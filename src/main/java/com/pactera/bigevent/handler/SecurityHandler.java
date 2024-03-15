package com.pactera.bigevent.handler;

import com.pactera.bigevent.common.entity.base.Result;
import com.pactera.bigevent.common.entity.constants.ErrorMessageConst;
import com.pactera.bigevent.common.entity.constants.ResponseCode;
import com.pactera.bigevent.exception.SystemException;
import com.pactera.bigevent.gen.dto.LoginUser;
import com.pactera.bigevent.gen.dto.UserWithRolesDto;
import com.pactera.bigevent.service.UserService;
import com.pactera.bigevent.utils.JwtUtil;
import com.pactera.bigevent.utils.WebUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.pactera.bigevent.common.entity.constants.RedisDefinition.LOGIN_USER_KEY_PREFIX;
import static com.pactera.bigevent.common.entity.constants.RedisDefinition.LOGIN_USER_KEY_TIME;

/**
 * @author <p>
 * 创建时间：2023/10/11 16:03
 */
@Component
public class SecurityHandler {

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserService userService;

    public static final String USER_NAME = "username";


    /**
     * 登录成功处理
     *
     * @param request        请求
     * @param response       响应
     * @param authentication 认证信息
     */
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        handlerOnAuthenticationSuccess(request, response, (LoginUser) authentication.getPrincipal());
    }

    public void handlerOnAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        Long userId = user.getUser().getUserId();
        HashMap<String, Object> map = new HashMap<>();
        map.put(USER_NAME, user.getUser().getUsername());
        map.put("roleNames", user.getUser().getRoleNames());
        String token = jwtUtil.genToken(map);
        stringRedisTemplate.opsForValue().set(LOGIN_USER_KEY_PREFIX + userId, token, LOGIN_USER_KEY_TIME, TimeUnit.HOURS);
        WebUtil.renderString(response, Result.success("登录成功", token).asJsonString());
    }


    /**
     * 登录失败处理
     */
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        WebUtil.renderString(response, Result.error(402, exception.getMessage()).asJsonString());
    }

    /**
     * 退出登录处理
     */
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = request.getHeader("Authorization");
        Map<String, Object> map = jwtUtil.parseToken(token);
        if (token == null) {
            WebUtil.renderString(response, Result.success("退出登录成功").asJsonString());
        }
        String username = String.valueOf(map.get("username"));
        UserWithRolesDto loginUser = userService.getLoginUser(username);
        if (loginUser == null) {
            WebUtil.renderString(response, Result.success("退出登录成功").asJsonString());
            return;
        }
        Long userId = loginUser.getUserId();
        stringRedisTemplate.opsForValue().getOperations().delete(LOGIN_USER_KEY_PREFIX + userId);
        WebUtil.renderString(response, Result.success("退出登录成功").asJsonString());
    }

    /**
     * 没有登录处理
     */
    public void onUnAuthenticated(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        response.setStatus(ResponseCode.AUTHENTICATION_FAILED);
        throw new SystemException(ErrorMessageConst.AUTHENTICATION_FAILED);
    }

    /**
     * 没有权限处理
     */
    public void onAccessDeny(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) {
        WebUtil.renderString(response, Result.error(ResponseCode.INVALID_AUTHORIZATION,
                ErrorMessageConst.INVALID_AUTHORIZATION).asJsonString());
    }
}
