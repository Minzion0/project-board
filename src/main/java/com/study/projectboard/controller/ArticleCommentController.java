package com.study.projectboard.controller;

import com.study.projectboard.dto.UserAccountDto;
import com.study.projectboard.dto.request.ArticleCommentRequest;
import com.study.projectboard.dto.request.ArticleRequest;
import com.study.projectboard.dto.security.BoardPrincipal;
import com.study.projectboard.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public String postNewArticleComment(
            @AuthenticationPrincipal BoardPrincipal boardPrincipal,
            ArticleCommentRequest articleCommentRequest){

        articleCommentService.saveArticleComment(
                articleCommentRequest.toDto(boardPrincipal.toDto()));
        return "redirect:/articles/"+articleCommentRequest.articleId();
    }

    @PostMapping("/{commentId}/delete")
    public String deleteArticleComment(
            @AuthenticationPrincipal BoardPrincipal boardPrincipal,
            @PathVariable Long commentId, Long articleId){
        articleCommentService.deleteArticleComment(commentId,boardPrincipal.toDto().userId());

        return "redirect:/articles/"+articleId;
    }


}
