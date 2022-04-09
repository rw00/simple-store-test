package com.rw.testapp.domain.products.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ProductArticleDefinition {
    @JsonProperty("art_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Positive
    private Long articleId;
    @JsonProperty("amount_of")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Positive
    private Integer amountOf;
}
