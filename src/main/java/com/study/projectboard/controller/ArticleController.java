package com.study.projectboard.controller;

import com.study.projectboard.domain.type.SearchType;
import com.study.projectboard.dto.ArticleWithCommentsDto;
import com.study.projectboard.dto.response.ArticleResponse;
import com.study.projectboard.dto.response.ArticleWithCommentResponse;
import com.study.projectboard.service.ArticleService;
import com.study.projectboard.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
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
    private final PaginationService paginationService;

    @GetMapping
    public String articles(ModelMap map,
                           @RequestParam(required = false) SearchType searchType,
                           @RequestParam(required = false) String searchValue,
                           @PageableDefault(size = 10,sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable ){


        Page<ArticleResponse> articles = articleService.searchArticles(searchType, searchValue, pageable).map(ArticleResponse::from);
        List<Integer> paginationBarNumbers = paginationService.getPaginationBarNumbers(articles.getNumber(), articles.getTotalPages());
        map.addAttribute("articles", articles);
        map.addAttribute("paginationBarNumbers", paginationBarNumbers);
        map.addAttribute("searchTypes",SearchType.values());

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

    @GetMapping("/search-hashtag")
    public String searchHashtag(ModelMap map,
                                @RequestParam(required = false) String searchValue,
                                @PageableDefault(size = 10,sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        Page<ArticleResponse> articleResponses = articleService.searchArticlesViaHashtag(searchValue, pageable).map(ArticleResponse::from);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(articleResponses.getNumber(), articleResponses.getTotalPages());
        List<String> hashtgs = articleService.getHashtgs();

        map.addAttribute("articles",articleResponses);
        map.addAttribute("paginationBarNumbers",barNumbers);
        map.addAttribute("searchType",SearchType.HASHTAG);
        map.addAttribute("hashtags",hashtgs);






        return "articles/search-hashtag";
    }

}
