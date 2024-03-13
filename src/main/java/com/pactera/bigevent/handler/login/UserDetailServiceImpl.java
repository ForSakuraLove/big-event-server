package com.pactera.bigevent.handler.login;

import com.pactera.bigevent.exception.SystemException;
import com.pactera.bigevent.gen.dto.LoginUser;
import com.pactera.bigevent.gen.dto.UserWithRolesDto;
import com.pactera.bigevent.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Resource
    private UserService userService;

    @Override
    public LoginUser loadUserByUsername(String username) throws UsernameNotFoundException {
        UserWithRolesDto user = userService.getLoginUser(username);
        if (user == null) {
            throw new SystemException("账号错误");
        }
        return new LoginUser(user);
    }

}
