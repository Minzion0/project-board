package com.study.projectboard.repository.querydsl;

import com.study.projectboard.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface ArticleRepositoryCustom {

    /**
     * @deprecated  해시태그 도메인을 새로 만들어서 더이상 사용할 필요는 없다.
     * @see HashtagRepositoryCustom#findAllHathtagNames
     */
    @Deprecated
    List<String> findAllDistinctHashtags();

    Page<Article> findByHashtagNames (Collection<String> hashtagName, Pageable pageable);
}
