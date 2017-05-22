package com.example.leon.article.api;

public class ApiFactory {

    private static ArticleService articleService;

    public static ArticleService getApi() {
        if (articleService == null) {
            synchronized (ApiFactory.class) {
                if (articleService == null) {
                    articleService = ServiceGenerator.createService(ArticleService.class);
                }
            }
        }
        return articleService;
    }
}
