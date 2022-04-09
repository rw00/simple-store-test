package com.rw.testapp.domain.articles.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.validation.Valid;
import lombok.Data;


@Data
public class ArticlesDefinitions {
    @JsonProperty("inventory")
    private List<@Valid ArticleDefinition> articleDefinitions;
}
