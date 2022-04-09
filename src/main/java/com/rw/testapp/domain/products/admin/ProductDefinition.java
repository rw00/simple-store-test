package com.rw.testapp.domain.products.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ProductDefinition {
    @NotBlank
    private String name;
    @JsonProperty("contain_articles")
    @Size(min = 1)
    private List<@Valid ProductArticleDefinition> articles;
}
