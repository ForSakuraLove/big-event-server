package com.pactera.bigevent.interceptor;

import com.pactera.bigevent.common.entity.CurrentUserContext;
import com.pactera.bigevent.gen.dto.UserWithRolesDto;
import com.pactera.bigevent.service.UserService;
import com.pactera.bigevent.utils.JwtUtil;
import com.pactera.bigevent.utils.ThreadLocalUserUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;
import java.util.Map;

import static com.pactera.bigevent.common.entity.constants.RedisDefinition.LOGIN_USER_KEY_PREFIX;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader("Authorization");
        try {
            Map<String, Object> map = jwtUtil.parseToken(token);
            if (map == null || map.isEmpty()) {
                throw new RuntimeException();
            }
            String username = String.valueOf(map.get("username"));
            UserWithRolesDto loginUser = userService.getLoginUser(username);
            if (loginUser == null || loginUser.getUserId() == null) {
                throw new RuntimeException();
            }
            Long userId = loginUser.getUserId();
            String redisToken = stringRedisTemplate.opsForValue().get(LOGIN_USER_KEY_PREFIX + userId);
            if (redisToken == null) {
                throw new RuntimeException();
            }
            if (!token.equals(redisToken)) {
                throw new RuntimeException();
            }
            CurrentUserContext currentUser = new CurrentUserContext();
            currentUser.setUserId(userId);
            currentUser.setUsername(username);
            currentUser.setRoleNames(List.of(loginUser.getRoleNames().split(",")));
            ThreadLocalUserUtil.setCurrentUserContext(currentUser);
            return true;
        } catch (Exception e) {
            response.setStatus(401);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUserUtil.remove();
    }
}
