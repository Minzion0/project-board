package com.study.projectboard.config;

import com.study.projectboard.dto.UserAccountDto;
import com.study.projectboard.dto.security.BoardPrincipal;
import com.study.projectboard.dto.security.KakaoOAuth2Response;
import com.study.projectboard.repository.UserAccountRepository;
import com.study.projectboard.service.UserAccountService;
import jakarta.servlet.DispatcherType;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import java.util.UUID;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,OAuth2UserService<OAuth2UserRequest,
            OAuth2User> oAuth2UserService ) throws Exception{
      return   http
                .authorizeHttpRequests(auth->auth
                        //정적 페이지 설정파일들 시큐리티에서 제외하는 명ㅣ
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers(
                                HttpMethod.GET,
                                "/",
                                "/articles",
                                "/articles/search-hashtag"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
              .formLogin(
                      withDefaults()
                ).logout(logout->logout
                      //로그 아웃 요청을 처리할 url
                      .logoutUrl("/logout")
                      //로그아웃 성공시 리다이렉트 할 경로
                      .logoutSuccessUrl("/")
              )//OAuth2로그인 인증 설정
              .oauth2Login(oAuth->oAuth
                      .userInfoEndpoint(userInfo->userInfo
                              .userService(oAuth2UserService)
                      )
              ).build();
    }



    @Bean
    public UserDetailsService userDetailsService(UserAccountService userAccountService){
        return username -> userAccountService
                .searchUser(username)
                .map(BoardPrincipal::form)
                .orElseThrow(()->new UsernameNotFoundException("유저 찾을수 없습니다. - username : {}"+ username));
    }
    //OAuth2 유저 인증 핵심
    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService(
            UserAccountService userAccountService,
            PasswordEncoder passwordEncoder
    ){
        final DefaultOAuth2UserService delegate= new DefaultOAuth2UserService();

        return userRequest -> {
            OAuth2User oAuth2User=delegate.loadUser(userRequest);

            KakaoOAuth2Response kakaoResponse = KakaoOAuth2Response.from(oAuth2User.getAttributes());
            String registrationId= userRequest.getClientRegistration().getRegistrationId();//kakao
            String providerId =String.valueOf(kakaoResponse.id());
            String username = registrationId+"_"+providerId;
            String dummyPassword =passwordEncoder.encode("{bcrypt}"+ UUID.randomUUID());


            return userAccountService.searchUser(username)
                    .map(BoardPrincipal::form)
                    .orElseGet(()->
                            BoardPrincipal.form(
                                    userAccountService.saveUser(
                                            username,
                                            dummyPassword,
                                            kakaoResponse.email(),
                                            kakaoResponse.nickname(),
                                            null
                                     )
                            )
                    );
        };
    }
    //암호화 모듈 생성
    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
