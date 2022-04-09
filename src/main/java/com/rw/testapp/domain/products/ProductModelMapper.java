package com.rw.testapp.domain.products;

import java.util.stream.Collectors;


public class ProductModelMapper {
    private ProductModelMapper() {
    }

    static Product entityToDTO(ProductEntity productEntity) {
        return Product.builder()
                .id(productEntity.getId())
                .name(productEntity.getName())
                .availableStock(productEntity.getProductArticlesDetailsEntities()
                        .stream()
                        .mapToInt(detail -> detail.getArticle().getStock() / detail.getAmountOf())
                        .min()
                        .orElse(0))
                .productArticlesDetails(productEntity.getProductArticlesDetailsEntities()
                        .stream()
                        .map(ProductModelMapper::articleRefEntityToDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    static ProductArticleDetails articleRefEntityToDTO(ProductArticleDetailsEntity productArticleDetailsEntity) {
        return ProductArticleDetails.builder()
                .articleId(productArticleDetailsEntity.getArticle().getId())
                .articleName(productArticleDetailsEntity.getArticle().getName())
                .articleStock(productArticleDetailsEntity.getArticle().getStock())
                .amountOf(productArticleDetailsEntity.getAmountOf())
                .build();
    }
}
