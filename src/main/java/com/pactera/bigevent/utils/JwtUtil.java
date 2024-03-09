package com.pactera.bigevent.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${security.jwt.prefix}")
    private String TOKEN_PREFIX;

    @Value("${security.jwt.expire}")
    private Long EXPIRE_TIME;

    @Value("${security.jwt.secret}")
    private String SECRET;

    //接收业务数据,生成token并返回
    public String genToken(Map<String, Object> claims) {
        return JWT.create()
                .withClaim("claims", claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .sign(Algorithm.HMAC256(SECRET.getBytes()));
    }

    //接收token,验证token,并返回业务数据
    public Map<String, Object> parseToken(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET.getBytes()))
                .build()
                .verify(token)
                .getClaim("claims")
                .asMap();
    }

}
