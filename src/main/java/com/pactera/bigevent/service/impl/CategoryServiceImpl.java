package com.pactera.bigevent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pactera.bigevent.gen.entity.Category;
import com.pactera.bigevent.mapper.CategoryMapper;
import com.pactera.bigevent.service.CategoryService;
import com.pactera.bigevent.utils.ThreadLocalUserUtil;
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
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public Integer saveCategory(Category category) {
        Long userId = ThreadLocalUserUtil.getUserId();
        category.setCreateUser(userId);
        category.setCreateTime(LocalDateTime.now());
        return categoryMapper.insert(category);
    }

    @Override
    public List<Category> getList() {
        Long userId = ThreadLocalUserUtil.getUserId();
        return categoryMapper.selectList(new QueryWrapper<Category>().lambda().eq(Category::getCreateUser, userId));
    }

    @Override
    public Integer updateCategory(Category category) {
        LambdaQueryWrapper<Category> queryWrapper = new QueryWrapper<Category>()
                .lambda()
                .eq(Category::getId, category.getId())
                .eq(Category::getIsDeleted, 0);
        Category oldCategory = categoryMapper.selectOne(queryWrapper);
        Long userId = ThreadLocalUserUtil.getUserId();
        oldCategory.setCategoryName(category.getCategoryName());
        oldCategory.setCategoryAlias(category.getCategoryAlias());
        oldCategory.setUpdateTime(LocalDateTime.now());
        oldCategory.setUpdateUser(userId);
        return categoryMapper.updateById(oldCategory);
    }

}
