package com.study.projectboard.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러")
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {
    private MockMvc mvc;

    public ArticleControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }
    @Disabled("구현중")
    @DisplayName("[view][Get] 게시글 리스트 (게시판) 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleleView_then()throws Exception{
        //given

        //when.then
        mvc.perform(get("/articles"))
                .andExpect(status().isOk())//200으로 오는지 확인
                .andExpect(content().contentType(MediaType.TEXT_HTML))//문자 타입으로 오는지 확인
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"));//넘어오는 model 안에 articles의 데이터가 넘어오는지.
    }

    @Disabled("구현중")
    @DisplayName("[view][Get] 게시글 상세 페이지 - 정상 호출")
    @Test
    public void givenNothing_ArticleleView_then()throws Exception{
        //given

        //when.then
        mvc.perform(get("/articles/1"))
                .andExpect(status().isOk())//200으로 오는지 확인
                .andExpect(content().contentType(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/detail"))
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("articleComments"));
    }
    @Disabled("구현중")
    @DisplayName("[view][Get] 게시글 검색 페이지 - 정상 호출")
    @Test
    public void givenNothing_ArticleleView_thenArticleSearchView()throws Exception{
        //given

        //when.then
        mvc.perform(get("/articles/search"))
                .andExpect(status().isOk())//200으로 오는지 확인
                .andExpect(content().contentType(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("articles/search"));
    }
    @Disabled("구현중")
    @DisplayName("[view][Get] 게시글 해시태그 검색 페이지 - 정상 호출")
    @Test
    public void givenNothing_ArticleleView_thenHashtag()throws Exception{
        //given

        //when.then
        mvc.perform(get("/articles/search-hashtag"))
                .andExpect(status().isOk())//200으로 오는지 확인
                .andExpect(content().contentType(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("articles/search-hashtag"));
    }
}