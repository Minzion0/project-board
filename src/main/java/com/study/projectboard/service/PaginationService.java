package com.study.projectboard.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class PaginationService {

    private static final int BAR_LENGTH = 5;


    public List<Integer> getPaginationBarNumbers(int currentPageNumber, int totalPages){
        /**
         * 페이징 중앙에 오는 수를 찾는 식
         * 현재 페이지에서 바의 길이 /2 한 수에서 빼주면 중앙에 올 수를 찾을수 있다.
         * 하지만 현재 페이지가 0~2 라면 음수가 나오기때문에 Math.Max 함수를 이용해서 음수가 되면 0을 반환하는 식으로 사용할것이다.
         */
        int startNumber = Math.max(currentPageNumber - (BAR_LENGTH /2),0);
        /**
         * 페이징 끝 페이지를 나타내는식
         * 처음 페이지에서 길이만큼 더해주면 쉽게 구할수 있다.더한값이 지금 존제하는 페이지 보다 클수 있기에
         * Math.min으로 둘의 합이 totlaPage보다 클수 없게 설정
         */
        int endNumber=Math.min(startNumber + BAR_LENGTH,totalPages);


        return IntStream.range(startNumber,endNumber).boxed().toList();
    }

    public int currentBarLength(){
        return BAR_LENGTH;
    }
}
