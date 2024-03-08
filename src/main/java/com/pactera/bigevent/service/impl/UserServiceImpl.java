package com.pactera.bigevent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pactera.bigevent.gen.User;
import com.pactera.bigevent.mapper.UserMapper;
import com.pactera.bigevent.service.UserService;
import com.pactera.bigevent.utils.Md5Util;
import com.pactera.bigevent.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

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
        user.setUpdateTime(LocalDateTime.now());

        return userMapper.insert(user);
    }

    @Override
    public Integer updateByUser(User user) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .lambda()
                .set(User::getNickname, user.getNickname())
                .set(User::getEmail, user.getEmail())
                .set(User::getUpdateTime, LocalDateTime.now())
                .eq(User::getId, user.getId());
        return userMapper.update(updateWrapper);
    }

    @Override
    public Integer updateAvatar(String avatarUrl) {

        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .lambda()
                .set(User::getUserPic, avatarUrl)
                .set(User::getUpdateTime, LocalDateTime.now())
                .eq(User::getId, id);
        return userMapper.update(updateWrapper);
    }
}
