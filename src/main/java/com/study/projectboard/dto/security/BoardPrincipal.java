package com.study.projectboard.dto.security;

import com.study.projectboard.dto.UserAccountDto;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public record BoardPrincipal(
        String userName,
        String password,
        Collection<? extends GrantedAuthority> authorities,
        String email,
        String nickname,
        String memo
) implements UserDetails {

    public static BoardPrincipal of(String userName, String password, String email, String nickname, String memo) {

        Set<RoleType> roleTypes = Set.of(RoleType.USER);

        return new BoardPrincipal(
                userName,
                password,
                roleTypes.stream()
                        .map(RoleType::getName).map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toUnmodifiableSet()),
                email,
                nickname
                ,memo
        );
    }

    public static BoardPrincipal form(UserAccountDto dto){
        return BoardPrincipal.of(
                dto.userId(),
                dto.userPassword(),
                dto.email(),
                dto.nickname(),
                dto.memo()
        );
    }

    public UserAccountDto toDto(){
        return UserAccountDto.of(
                userName,
                password,
                email,
                nickname,
                memo
                );
    }

    @Override //권한에 대한 설정
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }
// ------------여기 아래 부터는 설정 다할 필요없이 true 반환 해도 사용에 문제는 없다.----------------------
    @Override
    public boolean isAccountNonExpired() {return true;}
    @Override
    public boolean isAccountNonLocked() {return true;}
    @Override
    public boolean isCredentialsNonExpired() {return true;}
    @Override
    public boolean isEnabled() {return true;}



    public enum  RoleType {
        USER("ROLE_USER");
    @Getter
        public final String name;

        RoleType(String name) {
            this.name = name;
        }
    }





}
