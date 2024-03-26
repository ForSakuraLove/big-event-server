package com.pactera.bigevent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pactera.bigevent.gen.entity.UserRoleMapping;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 角色映射表 Mapper 接口
 * </p>
 *
 * @author zwk
 * @since 2024年03月19日
 */
@Mapper
public interface UserRoleMappingMapper extends BaseMapper<UserRoleMapping> {

}
