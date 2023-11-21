package com.playdata.articleindex.service;

import com.playdata.domain.articleindex.entity.ArticleIndex;
import com.playdata.domain.articleindex.repository.ArticleIndexRepository;
import com.playdata.domain.task.entity.ArticleCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class ArticleIndexServiceTest {

    @Autowired
    private ArticleIndexRepository articleIndexRepository;
    @Autowired
    private ArticleIndexService articleIndexService;


    @DisplayName("단어 목록과 카테고리로 연관있는 task id를 조회한다.")
    @Test
    void getRelatedTaskIds(){
        //given
        UUID task1 = UUID.randomUUID();
        UUID task2 = UUID.randomUUID();
        UUID task3 = UUID.randomUUID();
        UUID task4 = UUID.randomUUID();
        UUID task5 = UUID.randomUUID();

        ArticleIndex doctor = ArticleIndex.builder()
                .word("의사")
                .tasks(Set.of(task1, task2, task3))
                .category(ArticleCategory.EMPLOYMENT)
                .build();

        ArticleIndex study = ArticleIndex.builder()
                .word("공부")
                .tasks(Set.of(task3, task4, task5))
                .category(ArticleCategory.EMPLOYMENT)
                .build();

        ArticleIndex song = ArticleIndex.builder()
                .word("노래")
                .tasks(Set.of(task4, task5))
                .category(ArticleCategory.ART)
                .build();

        articleIndexRepository.saveAll(List.of(doctor, study, song));

        //when
        List<UUID> relatedTaskIds = articleIndexService.getRelatedTaskIds(
                List.of("의사", "공부"),
                ArticleCategory.EMPLOYMENT,
                5);

        //then
        assertThat(relatedTaskIds).hasSize(5)
                .containsExactlyInAnyOrder(task1, task2, task3, task4, task5);
    }

    @DisplayName("지정한 갯수보다 태스크가 적으면, 모든 태스크를 가져온다.")
    @Test
    void getRelatedTaskIdsLessCount(){
        //given
        UUID task1 = UUID.randomUUID();
        UUID task2 = UUID.randomUUID();
        UUID task3 = UUID.randomUUID();
        UUID task4 = UUID.randomUUID();
        UUID task5 = UUID.randomUUID();

        ArticleIndex doctor = ArticleIndex.builder()
                .word("의사")
                .tasks(Set.of(task1, task2, task3))
                .category(ArticleCategory.EMPLOYMENT)
                .build();

        ArticleIndex study = ArticleIndex.builder()
                .word("공부")
                .tasks(Set.of(task3, task4, task5))
                .category(ArticleCategory.EMPLOYMENT)
                .build();

        ArticleIndex song = ArticleIndex.builder()
                .word("노래")
                .tasks(Set.of(task4, task5))
                .category(ArticleCategory.ART)
                .build();

        articleIndexRepository.saveAll(List.of(doctor, study, song));

        //when
        List<UUID> relatedTaskIds = articleIndexService.getRelatedTaskIds(List.of("노래", "공부"), ArticleCategory.ART, 5);

        //then
        assertThat(relatedTaskIds).hasSize(2)
                .containsExactlyInAnyOrder(task4, task5);
    }

}