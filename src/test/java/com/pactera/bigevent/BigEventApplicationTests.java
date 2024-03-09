package com.pactera.bigevent;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pactera.bigevent.gen.entity.User;
import com.pactera.bigevent.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class BigEventApplicationTests {

    @Resource
    private UserMapper userMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void contextLoads() {
    }


    @Test
    void selectUser() {
        List<User> users = userMapper.selectList(new QueryWrapper<>());
        System.out.println(users);
    }

    @Test
    void testToken() {
        Map<String,Object> claims = new HashMap<>();
        claims.put("id",1);
        claims.put("name","pactera");
        String token = JWT.create()
                .withClaim("user", claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))//过期时间
                .sign(Algorithm.HMAC256("pactera"));//密钥
        System.out.println(token);
    }

    @Test
    void testTokenPass() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjbGFpbXMiOnsiaWQiOjQsInVzZXJuYW1lIjoid2FuZ2RlIn0sImV4cCI6MTcwOTM0NTIzMn0.cbQL9bnHCMf-qSV4KJZQ-iYXWBtUSfLVSDU4NfpSJAM";
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("pactera")).build();
        DecodedJWT jwt = jwtVerifier.verify(token);//验证token，生成一个解析的jwt对象
        Map<String, Claim> claims = jwt.getClaims();
        System.out.println(claims.get("username"));
    }

    @Test
    void testThreadLocal() {
        ThreadLocal<String> tl = new ThreadLocal<>();
        new Thread(() -> {
            tl.set("blue");
            System.out.println(Thread.currentThread().getName()+ tl.get());
            System.out.println(Thread.currentThread().getName()+ tl.get());
            System.out.println(Thread.currentThread().getName()+ tl.get());
            System.out.println(Thread.currentThread().getName()+ tl.get());
            System.out.println(Thread.currentThread().getName()+ tl.get());
            System.out.println(Thread.currentThread().getName()+ tl.get());
            System.out.println(Thread.currentThread().getName()+ tl.get());
            System.out.println(Thread.currentThread().getName()+ tl.get());
            System.out.println(Thread.currentThread().getName()+ tl.get());
            System.out.println(Thread.currentThread().getName()+ tl.get());
            System.out.println(Thread.currentThread().getName()+ tl.get());
            System.out.println(Thread.currentThread().getName()+ tl.get());
            System.out.println(Thread.currentThread().getName()+ tl.get());
            System.out.println(Thread.currentThread().getName()+ tl.get());
        },"蓝色").start();

        new Thread(() -> {
            tl.set("green");
            System.out.println(Thread.currentThread().getName()+ tl.get());
            System.out.println(Thread.currentThread().getName()+ tl.get());
            System.out.println(Thread.currentThread().getName()+ tl.get());
            System.out.println(Thread.currentThread().getName()+ tl.get());
            System.out.println(Thread.currentThread().getName()+ tl.get());
            System.out.println(Thread.currentThread().getName()+ tl.get());
            System.out.println(Thread.currentThread().getName()+ tl.get());
            System.out.println(Thread.currentThread().getName()+ tl.get());
            System.out.println(Thread.currentThread().getName()+ tl.get());
            System.out.println(Thread.currentThread().getName()+ tl.get());
            System.out.println(Thread.currentThread().getName()+ tl.get());
            System.out.println(Thread.currentThread().getName()+ tl.get());
        },"绿色").start();
    }

    @Test
    void testStringIs(){
        String s1 = " ";
        String s2 = "";
        String s3 = null;

        System.out.println(StringUtils.hasLength(s1));
        System.out.println(StringUtils.hasLength(s2));
        System.out.println(StringUtils.hasLength(s3));
        System.out.println();
        System.out.println(s1.isEmpty());
        System.out.println(s2.isEmpty());
        System.out.println(s3.isEmpty());
    }

    @Test
    void redisTest(){
        String key = "username";
        stringRedisTemplate.opsForValue().set(key,"zhangsan",24, TimeUnit.SECONDS);
        String value = stringRedisTemplate.opsForValue().get(key);
        System.out.println(value);
    }
}
