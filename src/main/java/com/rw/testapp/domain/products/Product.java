package com.rw.testapp.domain.products;

import java.util.List;
import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class Product {
    Long id;
    String name;
    Integer availableStock;
    List<ProductArticleDetails> productArticlesDetails;
}
