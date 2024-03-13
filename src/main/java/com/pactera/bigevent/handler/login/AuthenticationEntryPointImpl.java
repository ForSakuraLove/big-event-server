package com.pactera.bigevent.handler.login;

import com.alibaba.fastjson.JSON;
import com.pactera.bigevent.common.entity.base.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Result result = Result.error("认证失败");
        String messages = JSON.toJSONString(result);
        response.getWriter().print(messages);
    }

}
