package com.study.projectboard.controller;

import com.study.projectboard.dto.UserAccountDto;
import com.study.projectboard.dto.request.ArticleCommentRequest;
import com.study.projectboard.dto.request.ArticleRequest;
import com.study.projectboard.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@RequiredArgsConstructor
@RequestMapping("/comments")
@Controller
public class ArticleCommentController {

    private final ArticleCommentService articleCommentService;

    @PostMapping("/new")
    public String postNewArticleComment(ArticleCommentRequest articleCommentRequest){
        //TODO : 사용자 인증 정보가 필요하다
        articleCommentService.saveArticleComment(
                articleCommentRequest.toDto(UserAccountDto.of(
                        "minzino","pw","minzino12435@gmail.com",null,null)));
        return "redirect:/articles/"+articleCommentRequest.articleId();
    }

    @PostMapping("/{commentId}/delete")
    public String deleteArticleComment(@PathVariable Long commentId, Long articleId){
        articleCommentService.deleteArticleComment(commentId);

        return "redirect:/articles/"+articleId;
    }


}
