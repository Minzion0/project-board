package com.study.projectboard.repository;

import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.study.projectboard.domain.Article;
import com.study.projectboard.domain.QArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource// spring data rest 를 사용하기 위해 repository 에 해당 어노테이션을 넣어준다.
public interface ArticleRepository extends
        JpaRepository<Article,Long>,
        QuerydslPredicateExecutor<Article>,//엔티티 안에 있는 모든 필드에 대한 기본 검색 기능을 추가해준다.
        QuerydslBinderCustomizer<QArticle>//binder 에는 일반 클래스가 아니라 Q클래스를 넣어준다 Q클래스는 기존 엔티에 query sql이 자동으로 생성해준다.
{


    Page<Article> findByTitleContaining(String title, Pageable pageable);
    Page<Article> findByContentContaining(String content, Pageable pageable);
    Page<Article> findByUserAccount_UserIdContaining(String userId, Pageable pageable);
    Page<Article> findByUserAccount_NicknameContaining(String nickName, Pageable pageable);
    Page<Article> findByHashtag(String hashTag, Pageable pageable);





    @Override//말그대로 커스터 마이징한 검색을 구현할수있는 메소드
    default void customize(QuerydslBindings bindings, QArticle root){
        bindings.excludeUnlistedProperties(true);//
        bindings.including(root.title,root.content,root.hashtag,root.createdAt,root.createdBy);
        //bindings.bind(root.title).first((StringExpression::likeIgnoreCase)); // 검색 쿼리 like '${v}'로 검색
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase);// 검색 쿼리 like '%${v}%'
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);//string 이 아니라 datetime 타입이라 eq로 동일 검사를 진행 시분초 모두 같은것만 검색

    }
}
