package com.rw.testapp.domain.articles;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rw.testapp.domain.articles.admin.ArticleDefinition;
import com.rw.testapp.domain.articles.admin.ArticlesDefinitions;
import com.rw.testapp.util.StreamUtil;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ArticlesService {
    private final ArticlesRepository articlesRepository;
    private final Validator validator;
    private final ObjectMapper objectMapper;

    public ArticlesService(ArticlesRepository articlesRepository, Validator validator, ObjectMapper objectMapper) {
        this.articlesRepository = articlesRepository;
        this.validator = validator;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void uploadData(byte[] data) throws Exception {
        ArticlesDefinitions articlesDefinitions = objectMapper.readValue(data, ArticlesDefinitions.class);
        validateAndSave(articlesDefinitions);
    }

    @Transactional
    public void updateStockById(Long articleId, int remainingQuantity) {
        articlesRepository.updateStockById(articleId, remainingQuantity);
    }

    @Transactional(readOnly = true)
    public Map<Long, Integer> getStocksByIds(List<Long> ids) {
        return StreamUtil.stream(articlesRepository.findAllById(ids))
                .collect(Collectors.toMap(ArticleEntity::getId, ArticleEntity::getStock));
    }

    // @VisibleForTesting
    @Transactional
    public void validateAndSave(ArticlesDefinitions articlesDefinitions) {
        validate(articlesDefinitions);
        articlesDefinitions.getArticleDefinitions().forEach(this::updateOrCreate);
    }

    private void validate(ArticlesDefinitions articlesDefinitions) {
        Set<ConstraintViolation<ArticlesDefinitions>> constraintViolations = validator.validate(articlesDefinitions);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    private void updateOrCreate(ArticleDefinition articleDefinition) {
        ArticleEntity articleEntity = ArticleModelMapper.definitionToEntity(articleDefinition);
        articlesRepository.save(articleEntity);
    }
}
