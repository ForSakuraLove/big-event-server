package com.pactera.bigevent.config;

import com.pactera.bigevent.common.entity.constants.ErrorMessageConst;
import com.pactera.bigevent.handler.SecurityHandler;
import com.pactera.bigevent.handler.login.CustomOAuth2UserService;
import com.pactera.bigevent.handler.login.JWTAuthenticationTokenFilter;
import com.pactera.bigevent.utils.Md5Util;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;
import java.util.Objects;

import static com.pactera.bigevent.common.entity.constants.Role.*;
import static com.pactera.bigevent.common.entity.constants.Url.*;

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

    @Resource
    private CustomOAuth2UserService customOAuth2UserService;

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
                    log.error(username + ErrorMessageConst.INCORRECT_ACCOUNT_OR_PASSWORD);
                    throw new BadCredentialsException(ErrorMessageConst.INCORRECT_ACCOUNT_OR_PASSWORD);
                }
                if (loginUser.getAuthorities() == null) {
                    log.error(username + ErrorMessageConst.WITHOUT_ANY_PERMISSIONS);
                    throw new BadCredentialsException(ErrorMessageConst.WITHOUT_ANY_PERMISSIONS);
                }
                log.info(username + "登录认证成功");
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
                        .requestMatchers(URL_LOGIN, URL_REGISTER,
                                URL_MONTHLY_VISITORS, URL_LOGOUT)
                        .permitAll()//所有人都可以访问
                        .requestMatchers(URL_SYSTEM_MANAGE_ALL)
//                        .hasRole("ADMIN")
                        .access((systemManage, object) -> {
                            Collection<? extends GrantedAuthority> authorities = systemManage.get().getAuthorities();
                            for (GrantedAuthority authority : authorities) {
                                if (authority.getAuthority().equals(ROLE_ADMIN)) {
                                    return new AuthorizationDecision(true);
                                }
                            }
                            return new AuthorizationDecision(false);
                        })
                        .anyRequest()
                        .authenticated())
                .formLogin(formLogin -> formLogin
                        .loginProcessingUrl(URL_LOGIN)
                        .successHandler(securityHandler::onAuthenticationSuccess)
                        .failureHandler(securityHandler::onAuthenticationFailure))
                .logout(conf -> conf
                        .logoutUrl(URL_LOGOUT)
                        .logoutSuccessHandler(securityHandler::onLogoutSuccess)
                )
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(conf -> conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(conf -> conf
                        .authenticationEntryPoint(securityHandler::onUnAuthenticated)//请求未认证的接口
                        .accessDeniedHandler(securityHandler::onAccessDeny)//请求未授权的接口
                )
                .oauth2Login(conf -> conf
                        .loginPage(URL_GITHUB_LOGIN)
                        .userInfoEndpoint(config -> config
                                .userService(customOAuth2UserService))
                        .successHandler(securityHandler::githubAuthenticationSuccess));
        return httpSecurity.build();
    }
}
