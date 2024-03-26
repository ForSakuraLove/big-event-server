package com.pactera.bigevent.handler;

import com.pactera.bigevent.common.entity.base.Result;
import com.pactera.bigevent.common.entity.constants.ErrorMessageConst;
import com.pactera.bigevent.common.entity.constants.ResponseCode;
import com.pactera.bigevent.exception.SystemException;
import com.pactera.bigevent.gen.dto.LoginUser;
import com.pactera.bigevent.gen.dto.UserWithRolesDto;
import com.pactera.bigevent.service.OauthUserService;
import com.pactera.bigevent.service.UserService;
import com.pactera.bigevent.utils.JwtUtil;
import com.pactera.bigevent.utils.WebUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.pactera.bigevent.common.entity.constants.CommonConst.*;
import static com.pactera.bigevent.common.entity.constants.CommonMessage.*;
import static com.pactera.bigevent.common.entity.constants.HeaderConst.*;
import static com.pactera.bigevent.common.entity.constants.PlatformName.*;
import static com.pactera.bigevent.common.entity.constants.RedisDefinition.*;
import static com.pactera.bigevent.common.entity.constants.Url.*;

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

    @Resource
    private OauthUserService oauthUserService;


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

    /**
     * github授权成功处理
     *
     * @param request        请求
     * @param response       响应
     * @param authentication 认证信息
     */
    public void githubAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Map<String, Object> attributes = ((OAuth2AuthenticationToken) authentication).getPrincipal().getAttributes();
        String id = attributes.get("id").toString();
        UserWithRolesDto user = oauthUserService.getLoginUser(GITHUB, id);
        Long userId = user.getUserId();
        HashMap<String, Object> map = new HashMap<>();
        map.put(USER_NAME, user.getUsername());
        map.put(ROLE_NAMES, user.getRoleNames());
        loginOfCookie(request, response, userId, map);
        response.sendRedirect(HOME);
    }

    public void handlerOnAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        Long userId = user.getUser().getUserId();
        HashMap<String, Object> map = new HashMap<>();
        map.put(USER_NAME, user.getUser().getUsername());
        map.put(ROLE_NAMES, user.getUser().getRoleNames());
        loginOfCookie(request, response, userId, map);
        WebUtil.renderString(response, Result.success(LOGIN_SUCCESS).asJsonString());
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
        String token = request.getHeader(AUTHORIZATION);
        Map<String, Object> map = jwtUtil.parseToken(token);
        if (token == null) {
            WebUtil.renderString(response, Result.success(LOGOUT_SUCCESS).asJsonString());
        }
        String username = String.valueOf(map.get(USER_NAME));
        UserWithRolesDto loginUser = userService.getLoginUser(username);
        if (loginUser == null) {
            WebUtil.renderString(response, Result.success(LOGOUT_SUCCESS).asJsonString());
            return;
        }
        Long userId = loginUser.getUserId();
        stringRedisTemplate.opsForValue().getOperations().delete(LOGIN_USER_KEY_PREFIX + userId);
        Cookie cookie = new Cookie(TOKEN, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        WebUtil.renderString(response, Result.success(LOGOUT_SUCCESS).asJsonString());
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

    private void loginOfCookie(HttpServletRequest request, HttpServletResponse response, Long userId, HashMap<String, Object> map) {
        String token = jwtUtil.genToken(map);
        stringRedisTemplate.opsForValue().set(LOGIN_USER_KEY_PREFIX + userId, token, LOGIN_USER_KEY_TIME, TimeUnit.HOURS);
        Cookie cookie = new Cookie(TOKEN, token);
//        cookie.setHttpOnly(true);
        cookie.setSecure(true); // 仅在HTTPS连接下发送cookie
        cookie.setPath("/"); // 设置cookie路径
        cookie.setDomain(LOCALHOST);//设置cookie的域属性
        cookie.setMaxAge(24 * 60 * 60);//设置过期时间
        response.addCookie(cookie);
    }
}
