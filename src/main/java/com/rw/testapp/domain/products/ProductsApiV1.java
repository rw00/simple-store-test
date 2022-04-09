package com.rw.testapp.domain.products;

import java.util.List;
import javax.validation.constraints.Positive;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/v1/products")
@Validated
public class ProductsApiV1 {
    private final ProductsService productsService;

    ProductsApiV1(ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    List<Product> getAll() {
        return productsService.getAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    Product getById(@PathVariable @Positive Long id) throws ProductNotFoundException {
        return productsService.getById(id);
    }

    @PostMapping("/buy")
    void buyProductByName(@RequestParam @Positive Long id) {
        productsService.sellProductById(id);
    }
}
