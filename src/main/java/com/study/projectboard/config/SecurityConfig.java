package com.study.projectboard.config;

import com.study.projectboard.dto.UserAccountDto;
import com.study.projectboard.dto.security.BoardPrincipal;
import com.study.projectboard.repository.UserAccountRepository;
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
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
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
                ).formLogin(
                        login->login
                                .defaultSuccessUrl("/articles")
                                .permitAll()
                ).logout(logout->logout
                      //로그 아웃 요청을 처리할 url
                      .logoutUrl("/logout")
                      //로그아웃 성공시 리다이렉트 할 경로
                      .logoutSuccessUrl("/")
              ).build();
    }



    @Bean
    public UserDetailsService userDetailsService(UserAccountRepository userAccountRepository){
        return username -> userAccountRepository
                .findById(username)
                .map(UserAccountDto::from)
                .map(BoardPrincipal::form)
                .orElseThrow(()->new UsernameNotFoundException("유저 찾을수 없습니다. - username : {}"+ username));
    }
    //암호화 모듈 생성
    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
