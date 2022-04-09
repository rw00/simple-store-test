package com.rw.testapp.domain.products.admin;

import com.rw.testapp.domain.products.ProductsService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/v1/admin/products")
class AdminProductsApiV1 {
    private final ProductsService productsService;

    AdminProductsApiV1(ProductsService productsService) {
        this.productsService = productsService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    void upload(@RequestParam("file") MultipartFile file) throws Exception {
        productsService.uploadData(file.getBytes());
    }
}
