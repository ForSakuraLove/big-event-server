package com.pactera.bigevent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pactera.bigevent.gen.Category;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zwk
 * @since 2024年02月29日
 */
public interface CategoryService extends IService<Category> {

    Integer saveCategory(Category category);

    List<Category> getList();

    Integer updateCategory(Category category);
}
