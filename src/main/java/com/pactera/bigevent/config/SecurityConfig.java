package com.pactera.bigevent.config;

import com.pactera.bigevent.handler.SecurityHandler;
import com.pactera.bigevent.handler.login.AuthenticationEntryPointImpl;
import com.pactera.bigevent.handler.login.JWTAuthenticationTokenFilter;
import com.pactera.bigevent.utils.Md5Util;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Objects;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    @Resource
    private SecurityHandler securityHandler;

    @Resource
    private JWTAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Resource
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return Md5Util.getMD5String(rawPassword.toString());
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return encode(rawPassword).equals(encodedPassword);
            }
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                String username = authentication.getName();
                String password = authentication.getCredentials().toString();
                UserDetails loginUser = userDetailsService.loadUserByUsername(username);
                if (Objects.isNull(loginUser) || !passwordEncoder().matches(password, loginUser.getPassword())) {
                    log.error(username + "账号或者密码错误");
                    throw new BadCredentialsException("账号或者密码错误");
                }
                if (loginUser.getAuthorities() == null) {
                    log.error(username + "没有任何权限");
                    throw new BadCredentialsException("没有任何权限");
                }
                return new UsernamePasswordAuthenticationToken(loginUser, password, loginUser.getAuthorities());
            }

            @Override
            public boolean supports(Class<?> authentication) {
                return authentication.equals(UsernamePasswordAuthenticationToken.class);
            }
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(authentication -> authentication
                        .requestMatchers("/user/login", "/user/register","/monthlyVisitors","/user/logout")//不拦截login访问的路径
                        .permitAll()//所有人都可以访问
                        .anyRequest()
                        .authenticated())
                .formLogin(formLogin -> formLogin
                        .loginProcessingUrl("/user/login")//处理前端的请求，与form表单的action一致
                        .successHandler(securityHandler::onAuthenticationSuccess)
                        .failureHandler(securityHandler::onAuthenticationFailure))
                .logout(conf -> conf
                        // 登出页面
                        .logoutUrl("/user/logout")
                        // 退出登录处理
                        .logoutSuccessHandler(securityHandler::onLogoutSuccess)
                )
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(conf -> conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> {
                    exception.authenticationEntryPoint(new AuthenticationEntryPointImpl());//请求未认证的接口
                });
        return httpSecurity.build();
    }
}
