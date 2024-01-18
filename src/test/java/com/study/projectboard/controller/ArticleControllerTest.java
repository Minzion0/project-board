package com.study.projectboard.controller;

import com.study.projectboard.config.SecurityConfig;
import com.study.projectboard.domain.type.SearchType;
import com.study.projectboard.dto.ArticleWithCommentsDto;
import com.study.projectboard.dto.UserAccountDto;
import com.study.projectboard.service.ArticleService;
import com.study.projectboard.service.PaginationService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러")
@Import(SecurityConfig.class)
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {
    private MockMvc mvc;

    @MockBean
    private ArticleService articleService;
    @MockBean
    private PaginationService paginationService;

    public ArticleControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }


    @DisplayName("[view][Get] 게시글 리스트 (게시판) 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleleView_then()throws Exception{
        //given
        given(articleService.searchArticles(eq(null),eq(null),any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(),anyInt())).willReturn(List.of(0,1,2,3,4));
        //when.then
        mvc.perform(get("/articles"))
                .andExpect(status().isOk())//200으로 오는지 확인
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))//문자 타입으로 오는지 확인
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))//넘어오는 model 안에 articles의 데이터가 넘어오는지.
                .andExpect(model().attributeExists("paginationBarNumbers"))
                .andExpect(model().attributeExists("searchTypes"));

        then(articleService).should().searchArticles(eq(null),eq(null),any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(),anyInt());

    }

    @DisplayName("[view][Get] 게시글 리스트 (게시판) 페이지 - 검색어와 함께 호출")
    @Test
    public void givenSearchArticle_whenRequestingArticleleView_thenSearchArticleView()throws Exception{
        //given
        SearchType searchType = SearchType.TITLE;
        String searchValue = "title";

        given(articleService.searchArticles(eq(searchType),eq(searchValue),any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(),anyInt())).willReturn(List.of(0,1,2,3,4));
        //when.then
        mvc.perform(get("/articles")
                        .queryParam("searchType", searchType.name())
                        .queryParam("searchValue",searchValue)
                )
                .andExpect(status().isOk())//200으로 오는지 확인
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))//문자 타입으로 오는지 확인
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))//넘어오는 model 안에 articles의 데이터가 넘어오는지.
                .andExpect(model().attributeExists("searchTypes"));

        then(articleService).should().searchArticles(eq(searchType),eq(searchValue),any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(),anyInt());

    }


    @DisplayName("[view][Get] 게시글 상세 페이지 - 정상 호출")
    @Test
    public void givenNothing_ArticleleView_then()throws Exception{
        //given
        Long articleId=1L;
        given(articleService.getArticle(articleId)).willReturn(createArticleWithCommentsDto());
        //when.then
        mvc.perform(get("/articles/"+articleId))
                .andExpect(status().isOk())//200으로 오는지 확인
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/detail"))
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("articleComments"));

        then(articleService).should().getArticle(articleId);
    }

    @DisplayName("[view][Get] 게시글 리스트 (게시판) 페이지 - 페이징, 정렬 기능")
    @Test
    void given_when_then() throws Exception {
        String sortName= "title";
        String direction= "desc";
        int pageNumber=0;
        int pageSize=5;

        PageRequest pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.desc(sortName)));
        List<Integer> barNumbers = List.of(1,2,3,4,5);
        given(articleService.searchArticles(null,null,pageable)).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(pageable.getPageNumber(),Page.empty().getTotalPages())).willReturn(barNumbers);

        mvc.perform(
                get("/articles")
                        .queryParam("page",String.valueOf(pageNumber))
                        .queryParam("size",String.valueOf(pageSize))
                        .queryParam("sort",sortName+","+direction)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attribute("paginationBarNumbers",barNumbers));

        then(articleService).should().searchArticles(null,null,pageable);
        then(paginationService).should().getPaginationBarNumbers(pageable.getPageNumber(),Page.empty().getTotalPages());
    }

    @Disabled("구현중")
    @DisplayName("[view][Get] 게시글 검색 페이지 - 정상 호출")
    @Test
    public void givenNothing_ArticleleView_thenArticleSearchView()throws Exception{
        //given

        //when.then
        mvc.perform(get("/articles/search"))
                .andExpect(status().isOk())//200으로 오는지 확인
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/search"));
    }


    @DisplayName("[view][Get] 게시글 해시태그 검색 페이지 - 정상 호출")
    @Test
    public void givenNothing_ArticleleView_thenHashtag()throws Exception{
        //given
        given(articleService.searchArticlesViaHashtag(eq(null),any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(),anyInt())).willReturn(List.of(1,2,3,4,5));
        given(articleService.getHashtgs()).willReturn(List.of("#java","#spring","#boot"));
        //when.then
        mvc.perform(get("/articles/search-hashtag"))
                .andExpect(status().isOk())//200으로 오는지 확인
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/search-hashtag"))
                .andExpect(model().attributeExists("hashtags"))
                .andExpect(model().attributeExists("paginationBarNumbers"))
                .andExpect(model().attribute("searchType",SearchType.HASHTAG));

        then(articleService).should().searchArticlesViaHashtag(eq(null),any(Pageable.class));
        then(articleService).should().getHashtgs();
        then(paginationService).should().getPaginationBarNumbers(anyInt(),anyInt());

    }


    @DisplayName("[view][Get] 게시글 해시태그 검색 페이지 - 정상 호출,해시태그 입력")
    @Test
    public void givenHashtag_ArticleleView_thenHashtag()throws Exception{
        //given

        String searchHashtag = "#java";



        given(articleService.searchArticlesViaHashtag(eq(searchHashtag),any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(),anyInt())).willReturn(List.of(1,2,3,4,5));
        given(articleService.getHashtgs()).willReturn(List.of("#java","#spring","#boot"));
        //when.then
        mvc.perform(get("/articles/search-hashtag")
                        .queryParam("searchValue",searchHashtag)
                )
                .andExpect(status().isOk())//200으로 오는지 확인
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/search-hashtag"))
                .andExpect(model().attributeExists("hashtags"))
                .andExpect(model().attributeExists("paginationBarNumbers"))
                .andExpect(model().attribute("searchType",SearchType.HASHTAG));

        then(articleService).should().searchArticlesViaHashtag(eq(searchHashtag),any(Pageable.class));
        then(articleService).should().getHashtgs();
        then(paginationService).should().getPaginationBarNumbers(anyInt(),anyInt());

    }



    private ArticleWithCommentsDto createArticleWithCommentsDto(){
        return ArticleWithCommentsDto.of(
                1L,
                createUserAccountDto(),
                Set.of(),
                "title",
                "content",
                "#java",
                LocalDateTime.now(),
                "minzino",
                LocalDateTime.now(),
                "minzino"
        );
    }

    private UserAccountDto createUserAccountDto(){
        return UserAccountDto.of(
                1L,
                "minzino",
                "pw",
                "minzino@main.com",
                "minzino",
                "memo",
                LocalDateTime.now(),
                "minzino",
                LocalDateTime.now(),
                "minzino"
        );
    }
}