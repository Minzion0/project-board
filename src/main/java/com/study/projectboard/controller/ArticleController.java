package com.study.projectboard.controller;

import com.study.projectboard.domain.type.SearchType;
import com.study.projectboard.dto.ArticleDto;
import com.study.projectboard.dto.ArticleWithCommentsDto;
import com.study.projectboard.dto.response.ArticleCommentResponse;
import com.study.projectboard.dto.response.ArticleResponse;
import com.study.projectboard.dto.response.ArticleWithCommentResponse;
import com.study.projectboard.service.ArticleService;
import jakarta.persistence.Enumerated;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/articles")
@Controller
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public String articles(ModelMap map,
                           @RequestParam(required = false) SearchType searchType,
                           @RequestParam(required = false) String searchKeyword,
                           @PageableDefault(size = 10,sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable ){
        Page<ArticleResponse> articles = articleService.searchArticles(searchType, searchKeyword, pageable).map(ArticleResponse::from);
        map.addAttribute("articles", articles);
        return "articles/index";
    }

    @GetMapping("/{articleId}")
    public String article(@PathVariable Long articleId, ModelMap map){
        ArticleWithCommentsDto article = articleService.getArticle(articleId);
        ArticleWithCommentResponse commentResponse = ArticleWithCommentResponse.from(article);

        map.addAttribute("article",commentResponse);
        map.addAttribute("articleComments",commentResponse.articleCommentResponses());
        return "articles/detail";
    }

}
