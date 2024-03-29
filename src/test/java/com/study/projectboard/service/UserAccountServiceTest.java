package com.study.projectboard.service;

import com.study.projectboard.domain.UserAccount;
import com.study.projectboard.dto.UserAccountDto;
import com.study.projectboard.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("비즈니스 로직 - 회원")
@ExtendWith(MockitoExtension.class)
class UserAccountServiceTest {

    @InjectMocks
    private UserAccountService sut;

    @Mock
    private UserAccountRepository userAccountRepository;

    @DisplayName("존재하는 회원 ID를 검색하면, 회원데이터를 Optional로 반환")
    @Test
    void givenExistenUserId_whenSearching_thenReturnsOptionalUserData(){
        //given
        String username="minzino";
        given(userAccountRepository.findById(username)).willReturn(Optional.of(createUserAccount(username)));

        //when
        Optional<UserAccountDto> result = sut.searchUser(username);

        //then
        assertThat(result).isPresent();
        then(userAccountRepository).should().findById(username);
    }

    @DisplayName("존재하지 않는 회원ID를 검색하면 ,비어있는 Optional로 반환 한다")
    @Test
    void givenNonexistUserId_whenSearching_thenReturnsOptionalUserData(){
        //given
        String username="noooop";
        given(userAccountRepository.findById(username)).willReturn(Optional.empty());
        //when
        Optional<UserAccountDto> result = sut.searchUser(username);
        //then
        assertThat(result).isEmpty();
        then(userAccountRepository).should().findById(username);

    }

    @DisplayName("회원 정보를 입력하면, 새로운 회원 정보를 저장하여 가입시키고 해당 회원 데이터를 리턴한다.")
    @Test
    void givenUserParams_whenSaving_thenUserAccount(){
        //given
        UserAccount userAccount = createUserAccount("minzino");
        UserAccount savedUserAccount =createSigningUpUserAccount("minzino");
        given(userAccountRepository.save(userAccount)).willReturn(savedUserAccount);

        //when
        UserAccountDto result = sut.saveUser(
                userAccount.getUserId(),
                userAccount.getUserPassword(),
                userAccount.getEmail(),
                userAccount.getNickname(),
                userAccount.getMemo()
        );

        //then
        assertThat(result)
                .hasFieldOrPropertyWithValue("userId",userAccount.getUserId())
                .hasFieldOrPropertyWithValue("userPassword",userAccount.getUserPassword())
                .hasFieldOrPropertyWithValue("email",userAccount.getEmail())
                .hasFieldOrPropertyWithValue("nickname",userAccount.getNickname())
                .hasFieldOrPropertyWithValue("memo",userAccount.getMemo())
                .hasFieldOrPropertyWithValue("createdBy",userAccount.getUserId())
                .hasFieldOrPropertyWithValue("modifiedBy",userAccount.getUserId());

        then(userAccountRepository).should().save(userAccount);
    }

    private UserAccount createUserAccount(String username) {
        return createUserAccount(username,null);
    }

    private UserAccount createSigningUpUserAccount(String username) {
        return createUserAccount(username,username);
    }

    private UserAccount createUserAccount(String username,String createdBy){
        return UserAccount.of(
                username,
                "password",
                "test@gmail.com",
                "nickname",
                "memo",
                createdBy

        );
    }

}