package com.study.projectboard.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayName("비지니스 로직 - 페이지 네이션")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,classes = PaginationService.class)//무거운 스프링 부트 테스르를 가볍게 사용하는 옵션들
                                                                                                     //기본설정은 기존 스프링을 실행하여 테스트 하기에 무겁지만 none 을 해주면 무게를 줄일수 있고
                                                                                                    // classes로 적용한 클래스를 지정하거나, 아니면 void 클래스를 넣어주면 엄청 가벼운 환경으로 실행이 가능하다
                                                                                                    //하지만 지금은 외부에서 주입을 받아야함으로 service 클래스로 지정해 주었다
class PaginationServiceTest {

    private final PaginationService sut;

    public PaginationServiceTest(@Autowired PaginationService paginationService) {
        this.sut = paginationService;
    }

    /**
     *
     * @param currentPageNumber 현재 페이지 번호
     * @param totalPages 총 페이지 수
     * @param expected 검증하고 싶은 값
     */
    @DisplayName("현재 페이지 번호와 총 페이지 수를 주면 페이징 바 리스트를 반환한다.")
    @MethodSource
    @ParameterizedTest(name = "[{index}] {0},{1}=> {2}")// 값을 연속적으로 주입하여 여러번 테스팅을 할수 있는 태스트 어노테이션
    void givenCurrentPageNumberAndTotoalPages_whenCalculating_thenReturnsPaginationBarNumbers(int currentPageNumber, int totalPages, List<Integer> expected){

        List<Integer>actual=sut.getPaginationBarNumbers(currentPageNumber,totalPages);


        assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> givenCurrentPageNumberAndTotoalPages_whenCalculating_thenReturnsPaginationBarNumbers(){
        return Stream.of(
                arguments(0,13,List.of(0,1,2,3,4)),
                arguments(1,13,List.of(0,1,2,3,4)),
                arguments(2,13,List.of(0,1,2,3,4)),
                arguments(3,13,List.of(1,2,3,4,5)),
                arguments(4,13,List.of(2,3,4,5,6)),
                arguments(5,13,List.of(3,4,5,6,7)),
                arguments(10,13,List.of(8,9,10,11,12)),
                arguments(11,13,List.of(9,10,11,12)),
                arguments(12,13,List.of(10,11,12))
        );
    }
    @DisplayName("현재 설정되어 있는 페이지네이션 바의 길이를 알려준다.")
    @Test
    void given_when_then(){


        int barLength = sut.currentBarLength();

        assertThat(barLength).isEqualTo(5);//정의한 네이게이션 바의 길이가 원하는대로 5인지 확인
    }
}