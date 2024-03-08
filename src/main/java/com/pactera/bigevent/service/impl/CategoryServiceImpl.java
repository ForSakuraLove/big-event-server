package com.pactera.bigevent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pactera.bigevent.gen.Category;
import com.pactera.bigevent.mapper.CategoryMapper;
import com.pactera.bigevent.service.CategoryService;
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
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public Integer saveCategory(Category category) {

        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        category.setCreateUser(id);
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        return categoryMapper.insert(category);
    }

    @Override
    public List<Category> getList() {

        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        return categoryMapper.selectList(new QueryWrapper<Category>().lambda().eq(Category::getCreateUser, id));
    }

    @Override
    public Integer updateCategory(Category category) {

        category.setUpdateTime(LocalDateTime.now());
        return categoryMapper.updateById(category);
    }

}
