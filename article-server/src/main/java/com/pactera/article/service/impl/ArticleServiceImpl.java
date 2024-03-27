package com.pactera.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pactera.article.common.entity.PageBean;
import com.pactera.article.exception.SystemException;
import com.pactera.article.gen.entity.Article;
import com.pactera.article.mapper.ArticleMapper;
import com.pactera.article.service.ArticleService;
import com.pactera.article.utils.ThreadLocalUserUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zwk
 * @since 2024年02月29日
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    @Override
    public Integer saveArticle(Article article) {
        Long userId = ThreadLocalUserUtil.getUserId();
        article.setCreateTime(LocalDateTime.now());
        article.setCreateUser(userId);
        return articleMapper.insert(article);
    }

    @Override
    public PageBean<Article> getList(Integer pageNum, Integer pageSize, String categoryId, String state) {
        Long userId = ThreadLocalUserUtil.getUserId();
        PageHelper.startPage(pageNum, pageSize);
        List<Article> articleList = articleMapper.getList(userId, categoryId, state);
        PageInfo<Article> articlePage = new PageInfo<>(articleList);
        PageBean<Article> pageBean = new PageBean<>();
        pageBean.setTotal(articlePage.getTotal());
        pageBean.setItems(articlePage.getList());
        return pageBean;
    }

    @Override
    public Integer updateArticle(Article article) {
        Long userId = ThreadLocalUserUtil.getUserId();
        LambdaQueryWrapper<Article> queryWrapper = new QueryWrapper<Article>()
                .lambda()
                .eq(Article::getId, article.getId());
        Article oldArticle = articleMapper.selectOne(queryWrapper);
        if (oldArticle == null) {
            throw new SystemException("该文章为空");
        }
        oldArticle.setTitle(article.getTitle());
        oldArticle.setContent(article.getContent());
        oldArticle.setCategoryId(article.getCategoryId());
        oldArticle.setState(article.getState());
        oldArticle.setCoverImg(article.getCoverImg());
        oldArticle.setUpdateTime(LocalDateTime.now());
        oldArticle.setUpdateUser(userId);
        return articleMapper.updateById(oldArticle);
    }

    @Override
    public void deleteArticlesByCategoryId(Integer categoryId) {
        LambdaQueryWrapper<Article> queryWrapper = new QueryWrapper<Article>()
                .lambda()
                .eq(Article::getCategoryId, categoryId);
        articleMapper.delete(queryWrapper);
    }
}
