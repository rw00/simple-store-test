package com.rw.testapp;

import com.rw.testapp.domain.articles.ArticleEntity;
import com.rw.testapp.domain.articles.ArticlesRepository;
import com.rw.testapp.domain.articles.ArticlesService;
import com.rw.testapp.domain.articles.admin.ArticleDefinition;
import com.rw.testapp.domain.articles.admin.ArticlesDefinitions;
import com.rw.testapp.domain.products.Product;
import com.rw.testapp.domain.products.ProductArticlesDetailsRepository;
import com.rw.testapp.domain.products.ProductEntity;
import com.rw.testapp.domain.products.ProductsRepository;
import com.rw.testapp.domain.products.ProductsService;
import com.rw.testapp.domain.products.admin.ProductArticleDefinition;
import com.rw.testapp.domain.products.admin.ProductDefinition;
import com.rw.testapp.domain.products.admin.ProductsDefinitions;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.api.OptionalAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class SimpleStoreAppTests {
    private static final String TEST_PRODUCT_NAME = "table";
    private static final long TEST_ARTICLE_1_ID = 999L;
    private static final long TEST_ARTICLE_2_ID = 888L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private ProductsService productsService;

    @Autowired
    private ArticlesRepository articlesRepository;

    @Autowired
    private ArticlesService articlesService;

    @Autowired
    private ProductArticlesDetailsRepository productArticlesDetailsRepository;

    @BeforeEach
    void cleanUp() {
        productArticlesDetailsRepository.deleteAll();
        articlesRepository.deleteAll();
        productsRepository.deleteAll();
    }

    @Test
    void whenAdminLoadsProductsAndArticles_thenInventoryIsUpdated() throws Exception {
        uploadResources("inventory.json", "/v1/admin/articles");
        assertThat(articlesRepository.count()).isEqualTo(4);
        OptionalAssert<ArticleEntity> articleEntityOptionalAssert = assertThat(articlesRepository.findById(2L)).isPresent();
        articleEntityOptionalAssert.map(ArticleEntity::getName).hasValue("screw");
        articleEntityOptionalAssert.map(ArticleEntity::getStock).hasValue(17);

        uploadResources("products.json", "/v1/admin/products");
        Optional<ProductEntity> optionalProductEntity = productsRepository.findByName("Dining Chair");
        assertThat(optionalProductEntity).isPresent();
        Product product = productsService.getById(optionalProductEntity.get().getId());
        assertThat(product.getProductArticlesDetails()).hasSize(3);
        assertThat(productsRepository.count()).isEqualTo(2);
    }

    @Test
    void whenBuyingProduct_thenServiceChecksForAvailableStockAndUpdatesInventory() {
        initTestData();
        Optional<ProductEntity> optionalProductEntity = productsRepository.findByName(TEST_PRODUCT_NAME);
        assertThat(optionalProductEntity).isPresent();
        Long productId = optionalProductEntity.get().getId();
        productsService.sellProductById(productId);
        assertThat(articlesRepository.findById(TEST_ARTICLE_1_ID)).isPresent().map(ArticleEntity::getStock).hasValue(28);
        assertThat(articlesRepository.findById(TEST_ARTICLE_2_ID)).isPresent().map(ArticleEntity::getStock).hasValue(2);
        AbstractThrowableAssert<?, ? extends Throwable> throwableAssert = assertThatThrownBy(() -> productsService.sellProductById(productId));
        throwableAssert.hasMessageContaining("Product 'table' is out of stock");
    }

    @Test
    void whenUploadingInventoryDetails_thenServiceValidatesInput() throws Exception {
        Exception resolvedException = doUploadResources("invalid_inventory.json", "/v1/admin/articles")
                .andExpect(status().isBadRequest())
                .andReturn().getResolvedException();
        assertThat(resolvedException).isNotNull();
        assertThat(resolvedException.getMessage()).contains("name: must not be blank");
        assertThat(resolvedException.getMessage()).contains("id: must be greater than 0");
        assertThat(resolvedException.getMessage()).contains("stock: must be greater than or equal to 0");
    }

    @Test
    void whenGivenProductDoesNotExist_thenApiReturns404() throws Exception {
        Long testId = 12345L;

        mockMvc.perform(get(String.format("/v1/products/%d", testId)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value(String.format("Could not find product with ID=%d", testId)))
                .andExpect(jsonPath("$.statusCode").value("404"));
    }

    private void uploadResources(String resourceFileName, String restPath) throws Exception {
        doUploadResources(resourceFileName, restPath)
                .andExpect(status().isOk());
    }

    private ResultActions doUploadResources(String resourceFileName, String restPath) throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource(resourceFileName).toURI())));
        return mockMvc.perform(multipart(restPath)
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .content(file.getBytes()));
    }

    private void initTestData() {
        ArticlesDefinitions articlesDefinitions = new ArticlesDefinitions();
        articlesDefinitions.setArticleDefinitions(
                List.of(
                        ArticleDefinition.builder()
                                .id(TEST_ARTICLE_1_ID)
                                .name("leg")
                                .stock(32)
                                .build(),
                        ArticleDefinition.builder()
                                .id(TEST_ARTICLE_2_ID)
                                .name("screw")
                                .stock(18)
                                .build()
                )
        );
        articlesService.validateAndSave(articlesDefinitions);

        ProductsDefinitions productsDefinitions = new ProductsDefinitions();
        productsDefinitions.setProductDefinitions(
                List.of(
                        ProductDefinition.builder()
                                .name(TEST_PRODUCT_NAME)
                                .articles(
                                        List.of(
                                                ProductArticleDefinition.builder()
                                                        .articleId(TEST_ARTICLE_1_ID)
                                                        .amountOf(4)
                                                        .build(),
                                                ProductArticleDefinition.builder()
                                                        .articleId(TEST_ARTICLE_2_ID)
                                                        .amountOf(16)
                                                        .build())
                                )
                                .build()
                )
        );
        productsService.validateAndSave(productsDefinitions);
    }
}
