package com.rw.testapp.domain.products.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.validation.Valid;
import lombok.Data;


@Data
public class ProductsDefinitions {
    @JsonProperty("products")
    private List<@Valid ProductDefinition> productDefinitions;
}
