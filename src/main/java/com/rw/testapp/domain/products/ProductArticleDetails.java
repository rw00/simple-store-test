package com.rw.testapp.domain.products;

import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class ProductArticleDetails {
    Long articleId;
    String articleName;
    Integer articleStock;
    Integer amountOf;
}
