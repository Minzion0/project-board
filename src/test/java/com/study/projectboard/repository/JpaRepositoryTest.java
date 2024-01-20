package com.study.projectboard.repository;




import com.study.projectboard.config.JpaConfig;
import com.study.projectboard.domain.Article;
import com.study.projectboard.domain.UserAccount;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
//@ActiveProfiles("testdb")//프로필 변경
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)//이것을 적용해주어야 테스트 디비를 자동으로 연결하지 않고 yaml에서 설정한 경로로 연결

@DisplayName("JPA 연결 테스트")
@Import(JpaRepositoryTest.TestJpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {

    private final ArticleRepository articleRepository;
    private  final ArticleCommentRepository articleCommentRepository;
    private final UserAccountRepository userAccountRepository;

    public JpaRepositoryTest(
            @Autowired
            ArticleRepository articleRepository,
            @Autowired
            ArticleCommentRepository articleCommentRepository,
            @Autowired
            UserAccountRepository userAccountRepository
    ) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
        this.userAccountRepository=userAccountRepository;
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
        long previousCount = articleRepository.count();
        UserAccount userAccount = userAccountRepository.save(UserAccount.of("uno", "pw", null, null, null));
        Article article = Article.of(userAccount, "new article", "new content", "#spring");

        //when

        articleRepository.save(article);

        //then

        assertThat(articleRepository.count()).isEqualTo(previousCount + 1);
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

    @EnableJpaAuditing
    @TestConfiguration
    public static class TestJpaConfig{
        @Bean
        public AuditorAware<String > auditorAware(){
            return ()-> Optional.of("minzino");
        }

    }

}