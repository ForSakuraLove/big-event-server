package com.pactera.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pactera.article.common.entity.PageBean;
import com.pactera.article.gen.entity.Article;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zwk
 * @since 2024年02月29日
 */
public interface ArticleService extends IService<Article> {

    Integer saveArticle(Article article);

    PageBean<Article> getList(Integer pageNum, Integer pageSize, String categoryId, String state);

    Integer updateArticle(Article article);

    void deleteArticlesByCategoryId(Integer categoryId);
}
