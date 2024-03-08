package com.pactera.bigevent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pactera.bigevent.gen.Article;
import com.pactera.bigevent.gen.PageBean;
import com.pactera.bigevent.mapper.ArticleMapper;
import com.pactera.bigevent.service.ArticleService;
import com.pactera.bigevent.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        article.setCreateUser(id);
        return articleMapper.insert(article);
    }

    @Override
    public PageBean<Article> getList(Integer pageNum, Integer pageSize, String categoryId, String state) {

        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
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
        article.setUpdateTime(LocalDateTime.now());
        return articleMapper.updateById(article);
    }
}
