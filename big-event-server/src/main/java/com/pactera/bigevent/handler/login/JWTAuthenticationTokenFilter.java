package com.pactera.bigevent.handler.login;

import com.pactera.bigevent.common.entity.CurrentUserContext;
import com.pactera.bigevent.gen.dto.LoginUser;
import com.pactera.bigevent.gen.dto.UserWithRolesDto;
import com.pactera.bigevent.service.UserService;
import com.pactera.bigevent.utils.JwtUtil;
import com.pactera.bigevent.utils.ThreadLocalUserUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.pactera.bigevent.common.entity.constants.CommonConst.*;
import static com.pactera.bigevent.common.entity.constants.HeaderConst.*;
import static com.pactera.bigevent.common.entity.constants.RedisDefinition.*;

@Component
public class JWTAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private UserService userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(AUTHORIZATION);
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }
        Map<String, Object> map = jwtUtil.parseToken(token);
        if (map == null || map.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }
        String username = String.valueOf(map.get(USER_NAME));
        UserWithRolesDto loginUser = userService.getLoginUser(username);
        if (loginUser == null || loginUser.getUserId() == null) {
            filterChain.doFilter(request, response);
            return;
        }
        Long userId = loginUser.getUserId();
        String redisToken = stringRedisTemplate.opsForValue().get(LOGIN_USER_KEY_PREFIX + userId);
        if (redisToken == null) {
            filterChain.doFilter(request, response);
            return;
        }
        if (!token.equals(redisToken)) {
            filterChain.doFilter(request, response);
            return;
        }
        CurrentUserContext currentUser = new CurrentUserContext();
        currentUser.setUserId(userId);
        currentUser.setUsername(username);
        currentUser.setRoleNames(List.of(loginUser.getRoleNames().split(",")));
        ThreadLocalUserUtil.setCurrentUserContext(currentUser);
        LoginUser user = new LoginUser(loginUser);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

}
