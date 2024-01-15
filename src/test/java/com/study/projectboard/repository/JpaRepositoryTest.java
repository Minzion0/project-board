package com.study.projectboard.repository;




import com.study.projectboard.config.JpaConfig;
import com.study.projectboard.domain.Article;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
//@ActiveProfiles("testdb")//프로필 변경
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)//이것을 적용해주어야 테스트 디비를 자동으로 연결하지 않고 yaml에서 설정한 경로로 연결
@Disabled("Spring Data REST 통합테스트는 불필요함으로 제외시킴")
@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {

    private final ArticleRepository articleRepository;
    private  final ArticleCommentRepository articleCommentRepository;

    public JpaRepositoryTest(
            @Autowired
            ArticleRepository articleRepository,
            @Autowired
            ArticleCommentRepository articleCommentRepository
    ) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }


    @DisplayName("select테스트")
    @Test
    void givenTestData_whenSelecting_thenWorksFine(){
//        //given

//        //when
        List<Article> articles = articleRepository.findAll();
//        //then
        assertThat(articles)
                .isNotNull()
                .hasSize(123);
    }
    @DisplayName("insert 테스트")
    @Test
    void givenTestData_whenInserting_thenWorksFine(){
        //given
        long count = articleRepository.count();
        Article article = Article.of("new article", "new content", "#spring");
        //when

        Article save = articleRepository.save(article);



        //then
        assertThat(articleRepository.count()).isEqualTo(count+1);
    }

    @DisplayName("update 테스트")
    @Test
    void givenTestData_whenUpdating_thenWorksFine(){
        Article article = articleRepository.findById(1L).orElseThrow();
        String updateHashcode = "#spring boot";

        article.setHashtag(updateHashcode);

        Article save = articleRepository.saveAndFlush(article);

        assertThat(save.getHashtag()).isEqualTo(updateHashcode);
        assertThat(save.getId()).isEqualTo(article.getId());


    }

    @DisplayName("delete 테스트")
    @Test
    void givenTestData_whenDeleting_thenWorksFine(){
        Article article = articleRepository.findById(1L).orElseThrow();
        long articleCount = articleRepository.count();
        long articleCommentCount = articleCommentRepository.count();
        int deleteCommentSize = article.getArticleComments().size();

        articleRepository.delete(article);

        assertThat(articleRepository.count()).isEqualTo(articleCount-1);
        assertThat(articleCommentRepository.count()).isEqualTo(articleCommentCount-deleteCommentSize);



    }

}