package com.rw.testapp.domain.products;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductsRepository extends CrudRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByName(String name);
}
