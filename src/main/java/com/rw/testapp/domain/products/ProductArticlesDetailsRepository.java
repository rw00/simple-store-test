package com.rw.testapp.domain.products;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductArticlesDetailsRepository extends CrudRepository<ProductArticleDetailsEntity, Long> {
    ProductArticleDetailsEntity findByArticleIdAndProductId(Long articleId, Long productId);
}
