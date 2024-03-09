package com.pactera.bigevent.controller;

import com.pactera.bigevent.gen.entity.Article;
import com.pactera.bigevent.gen.entity.PageBean;
import com.pactera.bigevent.gen.entity.Result;
import com.pactera.bigevent.service.ArticleService;
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
            return Result.error("添加失败");
        }
        return Result.success();
    }

    @GetMapping
    public Result<PageBean<Article>> list(Integer pageNum, Integer pageSize,
                                          @RequestParam(required = false) String categoryId,
                                          @RequestParam(required = false) String state) {
        PageBean<Article> pageBean = articleService.getList(pageNum, pageSize, categoryId, state);
        return Result.success(pageBean);
    }

    @DeleteMapping
    public Result delete(@RequestParam Integer id) {
        boolean delete = articleService.removeById(id);
        if (!delete) {
            return Result.error("删除失败");
        }
        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody @Validated Article article) {
        Integer update = articleService.updateArticle(article);
        if (update != 1) {
            return Result.error("更新失败");
        }
        return Result.success();
    }
}
