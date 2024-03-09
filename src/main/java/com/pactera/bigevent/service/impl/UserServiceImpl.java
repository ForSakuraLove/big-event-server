package com.pactera.bigevent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pactera.bigevent.common.entity.CurrentUserContext;
import com.pactera.bigevent.exception.SystemException;
import com.pactera.bigevent.gen.entity.User;
import com.pactera.bigevent.mapper.UserMapper;
import com.pactera.bigevent.service.UserService;
import com.pactera.bigevent.utils.Md5Util;
import com.pactera.bigevent.utils.ThreadLocalUserUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author zwk
 * @since 2024年02月29日
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User findByUsername(String username) {
        LambdaQueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                .lambda()
                .eq(User::getUsername, username)
                .eq(User::getIsDeleted, 0);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public Integer register(String username, String password) {
        String md5Password = Md5Util.getMD5String(password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(md5Password);
        user.setCreateTime(LocalDateTime.now());
        return userMapper.insert(user);
    }

    @Override
    public Integer updateByUser(User user) {
        LambdaQueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                .lambda()
                .eq(User::getId, user.getId())
                .eq(User::getIsDeleted, 0);
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
    public CurrentUserContext initUserContext(String username) {
        return userMapper.InitUserContext(username);
    }
}
