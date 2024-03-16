package com.study.projectboard.service;

import com.study.projectboard.domain.Hashtag;
import com.study.projectboard.repository.HashtagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("비즈니스 로직 - 해시태그")
@ExtendWith(MockitoExtension.class)
class HashtagServiceTest {
    @InjectMocks //주입 받을 대상 1.-> 여기로 의존 주입
    private HashtagService sut;
    @Mock //목으로 주입할 것 1.
    private HashtagRepository hashtagRepository;

    //파라미터 테스트 여러가지의 케이스를 테스트를 하는것
    @DisplayName("본문을 파싱하면, 헤시태그 이름을 중복없이 반환")
    @MethodSource
    @ParameterizedTest(name = "[{index}] \"{0}\"=>{1}")
    void givenContent_whenParsing_thanReturnsUniqueHashtagNames(String input,Set<String> expected){
        //given


        //when
        Set<String> actual=sut.parseHashtagNames(input);

        //then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
        then(hashtagRepository).shouldHaveNoInteractions();
    }

    static Stream<Arguments> givenContent_whenParsing_thanReturnsUniqueHashtagNames(){
        return Stream.of(
                arguments(null,Set.of()),
                arguments("",Set.of()),
                arguments(" ",Set.of()),
                arguments("#",Set.of()),
                arguments("# ",Set.of()),
                arguments("  # ",Set.of()),
                arguments("  # ",Set.of()),
                arguments("#java",Set.of("java")),
                arguments("#java_spring",Set.of("java_spring")),
                arguments("#java#spring",Set.of("java","spring")),
                arguments("#java  #spring",Set.of("java","spring")),
                arguments("#java  #spring  ",Set.of("java","spring")),
                arguments("#-java-spring",Set.of()),
                arguments("#java#spring#부트",Set.of("java","spring","부트")),
                arguments("아주긴~~~~~~~~~~~클#java#spring#부트",Set.of("java","spring","부트")),
                arguments("아주긴~~~~~~~~~~~클#java#spring#부트~~~~~~~~~~~~~",Set.of("java","spring","부트"))
        );
    }

    @DisplayName("해시태그 이름들을 입력하면, 저장된 해시태그 중 이름에 매칭하는 것들을 중복없이 반환한다.")
    @Test
    void givenHashtagName_whenFindingHashtags_thenReturnsHashtagSet(){
        //given
        Set<String> hashtagNames = Set.of("java","spring","boots");
        given(hashtagRepository.findByHashtagNameIn(hashtagNames)).willReturn(List.of(
                Hashtag.of("java"),
                Hashtag.of("spring")

        ));
        //when
        Set<Hashtag> hashtags=sut.findHashtagsByNames(hashtagNames);

        //then
        assertThat(hashtags).hasSize(2);
        then(hashtagRepository).should().findByHashtagNameIn(hashtagNames);
    }


}