package com.pactera.bigevent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pactera.bigevent.gen.dto.UserWithRolesDto;
import com.pactera.bigevent.gen.entity.OauthUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 第三方授权用户表 Mapper 接口
 * </p>
 *
 * @author zwk
 * @since 2024年03月18日
 */
@Mapper
public interface OauthUserMapper extends BaseMapper<OauthUser> {

    UserWithRolesDto getLoginUser(String platformName, String thirdAccountId);

}
