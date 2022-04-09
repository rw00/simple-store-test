package com.rw.testapp.domain.articles.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ArticleDefinition {
    @JsonProperty("art_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Positive
    private Long id;
    @NotBlank
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @PositiveOrZero
    private Integer stock;
}
