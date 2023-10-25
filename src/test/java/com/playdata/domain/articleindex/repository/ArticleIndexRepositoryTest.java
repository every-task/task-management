package com.playdata.domain.articleindex.repository;

import com.playdata.domain.articleindex.entity.ArticleIndex;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class ArticleIndexRepositoryTest {

    @Autowired
    private ArticleIndexRepository articleIndexRepository;

    @DisplayName("단어 리스트로 색인 목록을 조회할 수 있다.")
    @Test
    void findByWordIn(){
        //given
        ArticleIndex doctor = ArticleIndex.builder().word("의사").build();
        ArticleIndex study = ArticleIndex.builder().word("공부").build();
        ArticleIndex sat = ArticleIndex.builder().word("수능").build();
        ArticleIndex university = ArticleIndex.builder().word("대학").build();

        articleIndexRepository.saveAll(List.of(doctor, study, sat, university));

        //when
        List<ArticleIndex> articleIndices = articleIndexRepository.findByWordIn(List.of("의사", "수능"));

        //then
        assertThat(articleIndices).hasSize(2)
                .extracting("word")
                .containsExactlyInAnyOrder("의사", "수능");
    }
}