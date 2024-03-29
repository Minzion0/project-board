package com.study.projectboard.dto.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
@DisplayName("DTO - KaKao OAuth 2.0 인증 응답 테스트")
class KakaoOAuth2ResponseTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @DisplayName("인증 결과를 Map(deserialized json)으로 받으면, 카카오 인증 응답 객체로 받는다")
    @Test
    void givenMapFromJson_whenInstantiating_thenReturnsKakaoResponseObject() throws Exception{
        String serializedResponse = """
                {
                    "id": 1234567890,
                    "connected_at": "2024-03-28T20:50:30Z",
                    "properties": {
                        "nickname": "이민용"
                    },
                    "kakao_account": {
                        "profile_nickname_needs_agreement": false,
                        "profile": {
                            "nickname": "이민용"
                        },
                        "has_email": true,
                        "email_needs_agreement": false,
                        "is_email_valid": true,
                        "is_email_verified": true,
                        "email": "test@gmail.com"
                    }
                }
                """;
        Map<String, Object> attributes = mapper.readValue(serializedResponse, new TypeReference<>() {});

        //when
        KakaoOAuth2Response result = KakaoOAuth2Response.from(attributes);

        //then
        assertThat(result)
                .hasFieldOrPropertyWithValue("id",1234567890L)
                .hasFieldOrPropertyWithValue("connectedAt", ZonedDateTime.of(2024,3,28,20,50,30,0, ZoneOffset.UTC)
                        .withZoneSameInstant(ZoneId.systemDefault())
                        .toLocalDateTime()
                )
                .hasFieldOrPropertyWithValue("kakaoAccount.profile.nickname","이민용")
                .hasFieldOrPropertyWithValue("kakaoAccount.email", "test@gmail.com");

    }

}