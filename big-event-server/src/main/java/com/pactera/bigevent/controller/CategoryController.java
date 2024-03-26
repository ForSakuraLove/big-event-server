package com.pactera.bigevent.controller;

import com.pactera.bigevent.common.entity.constants.ErrorMessageConst;
import com.pactera.bigevent.gen.entity.Category;
import com.pactera.bigevent.common.entity.base.Result;
import com.pactera.bigevent.service.CategoryService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zwk
 * @since 2024年02月29日
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @PostMapping
    public Result add(@RequestBody @Validated(Category.Add.class) Category category) {

        Integer save = categoryService.saveCategory(category);
        if (save != 1) {
            return Result.error(ErrorMessageConst.ADD_FAILED);
        }
        return Result.success("添加成功");
    }

    @GetMapping
    public Result<List<Category>> getList() {
        List<Category> list = categoryService.getList();
        return Result.success("获取分类成功", list);
    }

    @GetMapping("/detail")
    public Result<Category> getById(@RequestParam Integer id) {

        Category category = categoryService.getById(id);
        if (category == null) {
            return Result.error(ErrorMessageConst.ARTICLE_NOT_EXISTED);
        }
        return Result.success("获取文章成功", category);
    }

    @PutMapping
    public Result update(@RequestBody @Validated(Category.Update.class) Category category) {

        Integer update = categoryService.updateCategory(category);
        if (update != 1) {
            return Result.error(ErrorMessageConst.UPDATE_FAILED);
        }
        return Result.success("更新成功");
    }

    @DeleteMapping
    public Result delete(@RequestParam Integer id) {
        Integer delete = categoryService.logicDelete(id);
        if (delete != 1) {
            return Result.error(ErrorMessageConst.DELETE_FAILED);
        }
        return Result.success("删除成功");
    }
}
