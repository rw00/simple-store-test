package com.rw.testapp.domain.articles;

import com.rw.testapp.domain.articles.admin.ArticleDefinition;


public class ArticleModelMapper {
    private ArticleModelMapper() {
    }

    static ArticleEntity definitionToEntity(ArticleDefinition articleDefinition) {
        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setId(articleDefinition.getId());
        articleEntity.setName(articleDefinition.getName());
        articleEntity.setStock(articleDefinition.getStock());
        return articleEntity;
    }
}
