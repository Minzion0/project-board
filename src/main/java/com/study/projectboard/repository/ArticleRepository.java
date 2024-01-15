package com.study.projectboard.repository;

import com.study.projectboard.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource// spring data rest 를 사용하기 위해 repository 에 해당 어노테이션을 넣어준다.
public interface ArticleRepository extends JpaRepository<Article,Long> {
}
