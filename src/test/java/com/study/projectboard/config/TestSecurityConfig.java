package com.study.projectboard.config;

import com.study.projectboard.domain.UserAccount;
import com.study.projectboard.dto.UserAccountDto;
import com.study.projectboard.repository.UserAccountRepository;
import com.study.projectboard.service.UserAccountService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@Import(SecurityConfig.class)
public class TestSecurityConfig {

    @MockBean
    private UserAccountRepository userAccountRepository;
    @MockBean
    private UserAccountService userAccountService;

    @BeforeTestMethod
    //인즌 관련 테스트 할때 사용 하는 메소드
    public void securitySetup(){
        //테스트를 위한 테스트 전용 가상의 회원
        given(userAccountService.searchUser(anyString()))
                .willReturn(Optional.of(createUserAccountDto()));
        given(userAccountService.saveUser(anyString(),anyString(),anyString(),anyString(),anyString()))
                .willReturn(createUserAccountDto());
    }

    private UserAccountDto createUserAccountDto(){
        return UserAccountDto.of("minzinoTest",
                "pw",
                "minzinoTest@email.com",
                "minzinoTest",
                "test");

    }
}
