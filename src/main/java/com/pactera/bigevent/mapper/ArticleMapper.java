package com.pactera.bigevent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pactera.bigevent.gen.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zwk
 * @since 2024年02月29日
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    List<Article> getList(@Param("userId") Long userId, @Param("categoryId") String categoryId, @Param("state") String state);
}
