package com.pactera.bigevent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pactera.bigevent.gen.dto.UserWithRolesDto;
import com.pactera.bigevent.gen.entity.OauthUser;

/**
 * <p>
 * 第三方授权用户表 服务类
 * </p>
 *
 * @author zwk
 * @since 2024年03月18日
 */
public interface OauthUserService extends IService<OauthUser> {

    void loginByPlatformAndAccountId(String platformName, String thirdAccountId, String name);

    UserWithRolesDto getLoginUser(String platform, String id);

}
