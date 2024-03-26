package com.pactera.bigevent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pactera.bigevent.gen.dto.UserWithRolesDto;
import com.pactera.bigevent.gen.entity.OauthUser;
import com.pactera.bigevent.gen.entity.User;
import com.pactera.bigevent.gen.entity.UserRoleMapping;
import com.pactera.bigevent.mapper.OauthUserMapper;
import com.pactera.bigevent.mapper.UserRoleMappingMapper;
import com.pactera.bigevent.service.OauthUserService;
import com.pactera.bigevent.service.UserService;
import com.pactera.bigevent.utils.Md5Util;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * 第三方授权用户表 服务实现类
 * </p>
 *
 * @author zwk
 * @since 2024年03月18日
 */
@Service
@Transactional
public class OauthUserServiceImpl extends ServiceImpl<OauthUserMapper, OauthUser> implements OauthUserService {

    @Resource
    private OauthUserMapper oauthUserMapper;

    @Resource
    private UserService userService;

    @Resource
    private UserRoleMappingMapper userRoleMappingMapper;

    @Override
    public void loginByPlatformAndAccountId(String platformName, String thirdAccountId, String name) {
        QueryWrapper<OauthUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OauthUser::getPlatformName, platformName).eq(OauthUser::getThirdAccountId, thirdAccountId);
        OauthUser oauthUser = oauthUserMapper.selectOne(queryWrapper);
        if (oauthUser != null) {
            return;
        }
        User user = new User();
        String username = userService.generateUniqueUsername();
        user.setUsername(username);
        user.setNickname(name);
        String md5String = Md5Util.getMD5String(name + username.substring(1, 11));
        user.setPassword(md5String);
        user.setCreateUser(0L);
        user.setCreateTime(LocalDateTime.now());
        userService.save(user);
        User newUser = userService.getOne(new QueryWrapper<User>().lambda().eq(User::getUsername, username));
        oauthUser = new OauthUser();
        oauthUser.setUserId(newUser.getId());
        oauthUser.setPlatformName(platformName);
        oauthUser.setThirdAccountId(thirdAccountId);
        oauthUser.setCreateUser(0L);
        oauthUser.setCreateTime(LocalDateTime.now());
        UserRoleMapping userRoleMapping = new UserRoleMapping();
        userRoleMapping.setUserId(newUser.getId());
        userRoleMapping.setRoleId(1L);
        userRoleMappingMapper.insert(userRoleMapping);
        oauthUserMapper.insert(oauthUser);
    }

    @Override
    public UserWithRolesDto getLoginUser(String platform, String id) {
        return oauthUserMapper.getLoginUser(platform, id);
    }

}
