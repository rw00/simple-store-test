package com.rw.testapp.domain.articles.admin;

import com.rw.testapp.domain.articles.ArticlesService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/v1/admin/articles")
class AdminArticlesApiV1 {
    private final ArticlesService articlesService;

    AdminArticlesApiV1(ArticlesService articlesService) {
        this.articlesService = articlesService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    void upload(@RequestParam("file") MultipartFile file) throws Exception {
        articlesService.uploadData(file.getBytes());
    }
}
