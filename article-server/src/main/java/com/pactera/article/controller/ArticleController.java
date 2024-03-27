package com.pactera.article.controller;

import com.pactera.article.common.entity.PageBean;
import com.pactera.article.common.entity.base.Result;
import com.pactera.article.common.entity.constants.ErrorMessageConst;
import com.pactera.article.gen.entity.Article;
import com.pactera.article.service.ArticleService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zwk
 * @since 2024年02月29日
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @PostMapping
    public Result add(@RequestBody @Validated Article article) {
        Integer save = articleService.saveArticle(article);
        if (save != 1) {
            return Result.error(ErrorMessageConst.ADD_FAILED);
        }
        return Result.success("添加成功");
    }

    @GetMapping
    public Result<PageBean<Article>> list(Integer pageNum, Integer pageSize,
                                          @RequestParam(required = false) String categoryId,
                                          @RequestParam(required = false) String state) {
        PageBean<Article> pageBean = articleService.getList(pageNum, pageSize, categoryId, state);
        return Result.success("获取文章成功", pageBean);
    }

    @DeleteMapping
    public Result delete(@RequestParam Integer id) {
        boolean delete = articleService.removeById(id);
        if (!delete) {
            return Result.error(ErrorMessageConst.DELETE_FAILED);
        }
        return Result.success("删除成功");
    }

    @PutMapping
    public Result update(@RequestBody @Validated Article article) {
        Integer update = articleService.updateArticle(article);
        if (update != 1) {
            return Result.error(ErrorMessageConst.UPDATE_FAILED);
        }
        return Result.success("更新成功");
    }
    @DeleteMapping("/{categoryId}")
    public Result deleteArticlesByCategoryId(@PathVariable Integer categoryId) {
        articleService.deleteArticlesByCategoryId(categoryId);
        return Result.success("删除成功");
    }
}
