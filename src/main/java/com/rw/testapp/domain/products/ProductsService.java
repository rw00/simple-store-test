package com.rw.testapp.domain.products;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rw.testapp.domain.articles.ArticleEntity;
import com.rw.testapp.domain.articles.ArticlesService;
import com.rw.testapp.domain.products.admin.ProductArticleDefinition;
import com.rw.testapp.domain.products.admin.ProductDefinition;
import com.rw.testapp.domain.products.admin.ProductsDefinitions;
import com.rw.testapp.util.StreamUtil;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ProductsService {
    private final ProductsRepository productsRepository;
    private final ProductArticlesDetailsRepository productArticlesDetailsRepository;
    private final ArticlesService articlesService;
    private final Validator validator;
    private final ObjectMapper objectMapper;

    public ProductsService(ProductsRepository productsRepository, ProductArticlesDetailsRepository productArticlesDetailsRepository, ArticlesService articlesService, Validator validator, ObjectMapper objectMapper) {
        this.productsRepository = productsRepository;
        this.productArticlesDetailsRepository = productArticlesDetailsRepository;
        this.articlesService = articlesService;
        this.validator = validator;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void uploadData(byte[] data) throws Exception {
        ProductsDefinitions productsDefinitions = objectMapper.readValue(data, ProductsDefinitions.class);
        validateAndSave(productsDefinitions);
    }

    @Transactional(readOnly = true)
    public List<Product> getAll() {
        return StreamUtil.stream(productsRepository.findAll())
                .filter(this::isAvailableInStock)
                .map(ProductModelMapper::entityToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Product getById(Long id) throws ProductNotFoundException {
        Optional<ProductEntity> optionalProduct = productsRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            throw new ProductNotFoundException(String.format("Could not find product with ID=%d", id));
        }
        ProductEntity productEntity = optionalProduct.get();
        Hibernate.initialize(productEntity.getProductArticlesDetailsEntities());
        return ProductModelMapper.entityToDTO(productEntity);
    }

    @Transactional
    public void sellProductById(Long id) {
        Product product = getById(id);
        int quantity = 1;
        validateArticlesQuantities(product, quantity);
        sellAndUpdateInventory(product, quantity);
    }

    // @VisibleForTesting
    @Transactional
    public void validateAndSave(ProductsDefinitions productsDefinitions) {
        validate(productsDefinitions);
        productsDefinitions.getProductDefinitions().forEach(this::updateOrCreate);
    }

    private void validate(ProductsDefinitions productsDefinitions) {
        Set<ConstraintViolation<ProductsDefinitions>> constraintViolations = validator.validate(productsDefinitions);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    private void updateOrCreate(ProductDefinition productDefinition) {
        Optional<ProductEntity> optionalProductEntity = productsRepository.findByName(productDefinition.getName());
        ProductEntity productEntity;
        if (optionalProductEntity.isEmpty()) {
            productEntity = new ProductEntity();
            productEntity.setName(productDefinition.getName());
            productEntity = productsRepository.save(productEntity);
        } else {
            productEntity = optionalProductEntity.get();
        }
        Long productId = productEntity.getId();
        productDefinition.getArticles()
                .forEach(productArticleDefinition -> updateOrCreate(productId, productArticleDefinition));
    }

    private void updateOrCreate(Long productId, ProductArticleDefinition productArticleDefinition) {
        ProductArticleDetailsEntity productArticleDetailsEntity = productArticlesDetailsRepository.findByArticleIdAndProductId(productArticleDefinition.getArticleId(), productId);
        if (productArticleDetailsEntity == null) {
            productArticleDetailsEntity = new ProductArticleDetailsEntity();
            ArticleEntity articleEntity = new ArticleEntity();
            articleEntity.setId(productArticleDefinition.getArticleId());
            productArticleDetailsEntity.setArticle(articleEntity);
            ProductEntity productEntity = new ProductEntity();
            productEntity.setId(productId);
            productArticleDetailsEntity.setProduct(productEntity);
        }
        productArticleDetailsEntity.setAmountOf(productArticleDefinition.getAmountOf());
        productArticlesDetailsRepository.save(productArticleDetailsEntity);
    }

    private boolean isAvailableInStock(ProductEntity productEntity) {
        return productEntity.getProductArticlesDetailsEntities()
                .stream()
                .allMatch(it -> it.getArticle().getStock() >= it.getAmountOf());
    }

    private void validateArticlesQuantities(Product product, int quantity) {
        if (quantity > product.getAvailableStock()) {
            throw new OutOfStockException(String.format("Product '%s' is out of stock", product.getName()));
        }
    }

    private void sellAndUpdateInventory(Product product, int quantity) {
        List<ProductArticleDetails> productArticlesDetails = product.getProductArticlesDetails();
        productArticlesDetails.forEach(e -> {
            Long articleId = e.getArticleId();
            int remainingQuantity = e.getArticleStock() - quantity * e.getAmountOf();
            articlesService.updateStockById(articleId, remainingQuantity);
        });
    }
}
