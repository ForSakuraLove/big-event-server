package com.pactera.bigevent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pactera.bigevent.gen.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author zwk
 * @since 2024年02月29日
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
