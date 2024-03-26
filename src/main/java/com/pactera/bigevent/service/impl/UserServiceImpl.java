package com.pactera.bigevent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pactera.bigevent.exception.SystemException;
import com.pactera.bigevent.gen.dto.UserWithRolesDto;
import com.pactera.bigevent.gen.entity.User;
import com.pactera.bigevent.gen.entity.UserRoleMapping;
import com.pactera.bigevent.mapper.UserMapper;
import com.pactera.bigevent.mapper.UserRoleMappingMapper;
import com.pactera.bigevent.service.UserService;
import com.pactera.bigevent.utils.Md5Util;
import com.pactera.bigevent.utils.ThreadLocalUserUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author zwk
 * @since 2024年02月29日
 */
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMappingMapper userRoleMappingMapper;

    @Override
    public User findByUsername(String username) {
        LambdaQueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                .lambda()
                .eq(User::getUsername, username);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public Integer register(String username, String password) {
        String md5Password = Md5Util.getMD5String(password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(md5Password);
        user.setCreateTime(LocalDateTime.now());
        UserRoleMapping userRoleMapping = new UserRoleMapping();
        int insert = userMapper.insert(user);
        User user0 = userMapper.selectOne(new QueryWrapper<User>().lambda()
                .eq(User::getUsername, username));
        userRoleMapping.setUserId(user0.getId());
        userRoleMapping.setRoleId(1L);
        userRoleMappingMapper.insert(userRoleMapping);
        return insert;
    }

    @Override
    public Integer updateByUser(User user) {
        LambdaQueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                .lambda()
                .eq(User::getId, user.getId());
        User oldUser = userMapper.selectOne(queryWrapper);
        if (oldUser == null) {
            throw new SystemException("用户为空");
        }
        Long userId = ThreadLocalUserUtil.getUserId();
        oldUser.setNickname(user.getNickname());
        oldUser.setEmail(user.getEmail());
        oldUser.setUpdateUser(userId);
        oldUser.setUpdateTime(LocalDateTime.now());
        return userMapper.updateById(oldUser);
    }

    @Override
    public Integer updateAvatar(String avatarUrl) {
        Long userId = ThreadLocalUserUtil.getUserId();
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .lambda()
                .set(User::getUserPic, avatarUrl)
                .set(User::getUpdateTime, LocalDateTime.now())
                .set(User::getUpdateUser, userId)
                .eq(User::getId, userId);
        return userMapper.update(updateWrapper);
    }

    @Override
    public UserWithRolesDto getLoginUser(String username) {
        return userMapper.getLoginUser(username);
    }

    @Override
    public String generateUniqueUsername() {
        String userName = UUID.randomUUID().toString();
        List<User> userList = userMapper.selectList(null);
        while (!isUsernameUnique(userName, userList)) {
            userName = UUID.randomUUID().toString();
        }
        return userName.replace("-", "");
    }

    /**
     * 判断本次username是否唯一
     *
     * @param userName
     * @param userList
     * @return
     */
    private boolean isUsernameUnique(String userName, List<User> userList) {
        for (User user : userList) {
            if (user.getUsername().equals(userName)) {
                return false;
            }
        }
        return true;
    }

}
