package com.study.projectboard.service;

import com.study.projectboard.dto.ArticleCommentDto;
import com.study.projectboard.repository.ArticleCommentRepository;
import com.study.projectboard.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
@RequiredArgsConstructor
@Service
public class ArticleCommentServices {

    private final ArticleCommentRepository articleCommentRepository;
    private final ArticleRepository articleRepository;
    @Transactional(readOnly = true)
    public List<ArticleCommentDto> searchArticleComments(Long articleId) {
        return List.of();
    }

    public void saveArticleComment(ArticleCommentDto dto) {

    }

    public void updateArticleComment(ArticleCommentDto dto) {
    }

    public void deleteArticleComment(Long articleCommentId) {
        
    }
}
