package com.pactera.category.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pactera.category.exception.SystemException;
import com.pactera.category.gen.entity.Category;
import com.pactera.category.mapper.CategoryMapper;
import com.pactera.category.service.CategoryService;
import com.pactera.category.utils.ThreadLocalUserUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static com.pactera.category.common.entity.constants.Url.ARTICLE_SERVER;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zwk
 * @since 2024年02月29日
 */
@Transactional
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private RestTemplate restTemplate;

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
                .eq(Category::getId, category.getId());
        Category oldCategory = categoryMapper.selectOne(queryWrapper);
        if (oldCategory == null) {
            throw new SystemException("该分类为空");
        }
        Long userId = ThreadLocalUserUtil.getUserId();
        oldCategory.setCategoryName(category.getCategoryName());
        oldCategory.setCategoryAlias(category.getCategoryAlias());
        oldCategory.setUpdateTime(LocalDateTime.now());
        oldCategory.setUpdateUser(userId);
        return categoryMapper.updateById(oldCategory);
    }

    @Override
    public Integer logicDelete(Integer id) {
        int isDelete = categoryMapper.deleteById(id);
        String url = ARTICLE_SERVER + id;
        restTemplate.delete(url);
        return isDelete;
    }

}