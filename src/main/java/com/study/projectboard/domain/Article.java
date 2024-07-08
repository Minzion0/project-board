package com.study.projectboard.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@ToString(callSuper = true)
// 테이블과 인덱스를 정의하는 애노테이션입니다.
@Table(indexes = {
    @Index(columnList = "title"),
    @Index(columnList = "createdAt"),
    @Index(columnList = "createdBy")
})
// 엔티티를 정의하는 애노테이션입니다.
@Entity
public class Article extends AuditingFields {

    // ID 필드이며 자동 생성 전략을 IDENTITY로 설정합니다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Setter 애노테이션을 사용하여 필드의 Setter 메소드를 자동 생성합니다.
    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "userId")
    private UserAccount userAccount; // 유저 정보 (ID)

    @Setter
    @Column(nullable = false)
    private String title; // 제목

    @Setter
    @Column(nullable = false, length = 10000)
    private String content; // 본문

    // ToString 메소드에서 제외
    @ToString.Exclude
    @Setter
    // cascade 옵션을 insert, update 할 때만 사용하겠다는 옵션
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        // 관계의 주인 조인 테이블 생성
        name = "article_hashtag", // 테이블 이름
        joinColumns = @JoinColumn(name = "articleId"), // 컬럼명
        inverseJoinColumns = @JoinColumn(name = "hashtagId") // 조인할 상대 컬럼 이름
    )
    private Set<Hashtag> hashtags = new LinkedHashSet<>(); // 헤시태그

    // ToString 메소드에서 제외
    @ToString.Exclude
    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();

    // 기본 생성자입니다.
    protected Article() {}

    // 모든 필드를 초기화하는 생성자입니다.
    private Article(UserAccount userAccount, String title, String content) {
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
    }

    // 정적 팩토리 메소드로 Article 객체를 생성합니다.
    public static Article of(UserAccount userAccount, String title, String content) {
        return new Article(userAccount, title, content);
    }

    // 해시태그 관리 편의 메소드

    /**
     * 해시태그 하나 추가
     * @param hashtag
     */
    public void addHashtags(Hashtag hashtag) {
        this.getHashtags().add(hashtag);
    }

    /**
     * 컬렉션 단위 해시태그 추가
     * @param hashtags
     */
    public void addHashtags(Collection<Hashtag> hashtags) {
        this.getHashtags().addAll(hashtags);
    }

    /**
     * 해당 게시글 연관된 태그 비우기
     */
    public void clearHashtags() {
        this.getHashtags().clear();
    }

    // equals 메소드입니다. id가 동일한 경우 동일 객체로 간주합니다.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
        return this.getId() != null && this.getId().equals(article.getId());
    }

    // hashCode 메소드입니다. id의 해시코드를 반환합니다.
    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}