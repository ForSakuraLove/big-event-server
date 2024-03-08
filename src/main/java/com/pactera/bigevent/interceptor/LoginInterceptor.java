package com.pactera.bigevent.interceptor;

import com.pactera.bigevent.utils.JwtUtil;
import com.pactera.bigevent.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

import static com.pactera.bigevent.gen.RedisDefinition.LOGIN_USER_KEY_PREFIX;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader("Authorization");
        try {
            Map<String, Object> map = JwtUtil.parseToken(token);
            if(map == null || map.isEmpty()){
                throw new RuntimeException();
            }
            Integer id = (Integer) map.get("id");
            String redisToken = stringRedisTemplate.opsForValue().get(LOGIN_USER_KEY_PREFIX + id);
            if (redisToken == null) {
                throw new RuntimeException();
            }
            if (!token.equals(redisToken)) {
                throw new RuntimeException();
            }
            ThreadLocalUtil.set(map);
            return true;
        } catch (Exception e) {
            response.setStatus(401);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.remove();
    }
}
