package com.pactera.bigevent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pactera.bigevent.gen.User;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author zwk
 * @since 2024年02月29日
 */
public interface UserService extends IService<User> {

    User findByUsername(String username);

    Integer register(String username, String password);

    Integer updateByUser(User user);

    Integer updateAvatar(String avatarUrl);
}
