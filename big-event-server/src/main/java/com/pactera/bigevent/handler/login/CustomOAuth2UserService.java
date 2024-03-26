package com.pactera.bigevent.handler.login;

import com.pactera.bigevent.service.OauthUserService;
import jakarta.annotation.Resource;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.pactera.bigevent.common.entity.constants.PlatformName.*;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Resource
    private OauthUserService oauthUserService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String name = oAuth2User.getName();
        String thirdAccountId = attributes.get("id").toString();
        String avatarUrl = attributes.get("avatar_url").toString();
        oauthUserService.loginByPlatformAndAccountId(GITHUB, thirdAccountId, name, avatarUrl);
        return oAuth2User;
    }

}
