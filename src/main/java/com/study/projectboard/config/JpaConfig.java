package com.study.projectboard.config;

import com.study.projectboard.dto.security.BoardPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaConfig {

    @Bean
    public AuditorAware<String> auditorAware(){
                                                    //인증정보를 관리하는  SecurityContextHolder
        return ()-> Optional.ofNullable(SecurityContextHolder.getContext())
                   //  Context  안에있는     Authentication 찾아온다
                .map(SecurityContext::getAuthentication)
                // 찾아온 Authentication 인증이 된 계정인지 확인한다.
                .filter(Authentication::isAuthenticated)
                // 인증 된 계정이면 userDetail 을 찾아온다
                .map(Authentication::getPrincipal)
                // 받아온 정보를 cast를 통해 형변환 해준다
                .map(BoardPrincipal.class::cast)
                //그다음 유저 정보를 받아온다.
                .map(BoardPrincipal::getUsername);

    }
}
