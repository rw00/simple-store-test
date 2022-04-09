package com.rw.testapp.domain.articles;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface ArticlesRepository extends CrudRepository<ArticleEntity, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE ArticleEntity SET stock = :stock WHERE id = :id")
    void updateStockById(@Param("id") Long id, @Param("stock") Integer stock);
}
