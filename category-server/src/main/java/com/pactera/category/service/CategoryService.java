package com.pactera.category.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pactera.category.gen.entity.Category;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zwk
 * @since 2024年02月29日
 */
public interface CategoryService extends IService<Category> {

    Integer saveCategory(Category category);

    List<Category> getList();

    Integer updateCategory(Category category);

    Integer logicDelete(Integer id);
}
