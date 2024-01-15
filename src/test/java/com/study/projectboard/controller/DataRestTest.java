package com.study.projectboard.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest//controller 테스트시 사용하는 어노테이션
@DisplayName("Data REST - api 테스트")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public class DataRestTest {

    private MockMvc mvc;

    public DataRestTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }
    @DisplayName("[api} 게시글 리스트 조회 ")
    @Test
    void givenNoting_whenRequstingArticles_thenReturnsArticlesJsonResponse()throws Exception{
        mvc.perform(get("/api/articles"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.valueOf("application/hal+json")));
    }

    @DisplayName("[api} 게시글 단건 조회 ")
    @Test
    void givenNoting_whenRequstingArticlesInDetail_thenReturnsArticlesJsonResponse()throws Exception{
        mvc.perform(get("/api/articles/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.valueOf("application/hal+json")))
                ;
    }

    @DisplayName("[api} 게시글 -> 댓글리스트 조회 ")
    @Test
    void givenNoting_whenRequstingArticlesInCommentl_thenReturnsArticlesJsonResponse()throws Exception{
        mvc.perform(get("/api/articles/1/articleComments"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.valueOf("application/hal+json")));
    }

    @DisplayName("[api} 댓글리스트 조회 ")
    @Test
    void givenNoting_whenRequstingArticlesCommentl_thenReturnsArticlesJsonResponse()throws Exception{
        mvc.perform(get("/api/articleComments"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.valueOf("application/hal+json")));
    }

    @DisplayName("[api} 댓글단건 조회 ")
    @Test
    void givenNoting_whenRequstingArticlesCommentlDetail_thenReturnsArticlesJsonResponse()throws Exception{
        mvc.perform(get("/api/articleComments/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.valueOf("application/hal+json")));
    }
}
