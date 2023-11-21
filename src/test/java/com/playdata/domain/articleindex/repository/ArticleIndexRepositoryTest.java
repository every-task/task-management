package com.playdata.domain.articleindex.repository;

import com.playdata.domain.articleindex.entity.ArticleIndex;
import com.playdata.domain.task.entity.ArticleCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ActiveProfiles("test")
class ArticleIndexRepositoryTest {

    @Autowired
    private ArticleIndexRepository articleIndexRepository;

    @DisplayName("카테고리와 단어 리스트로 색인 목록을 조회할 수 있다.")
    @Test
    void findByWordIn(){
        //given
        ArticleIndex doctor = ArticleIndex.builder()
                .word("의사")
                .category(ArticleCategory.EMPLOYMENT)
                .build();

        ArticleIndex study = ArticleIndex.builder()
                .word("공부")
                .category(ArticleCategory.STRESS)
                .build();

        ArticleIndex sat = ArticleIndex.builder()
                .word("수능")
                .category(ArticleCategory.STRESS)
                .build();

        ArticleIndex university = ArticleIndex.builder()
                .word("대학")
                .category(ArticleCategory.EMPLOYMENT)
                .build();

        articleIndexRepository.saveAll(List.of(doctor, study, sat, university));

        //when
        List<ArticleIndex> articleIndices = articleIndexRepository.findByWordInAndCategory(List.of("의사", "대학"), ArticleCategory.EMPLOYMENT);

        //then
        assertThat(articleIndices).hasSize(2)
                .extracting("word")
                .containsExactlyInAnyOrder("의사", "대학");
    }
}