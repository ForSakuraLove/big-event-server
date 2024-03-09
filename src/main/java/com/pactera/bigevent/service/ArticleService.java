package com.pactera.bigevent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pactera.bigevent.gen.entity.Article;
import com.pactera.bigevent.common.entity.PageBean;

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
}
