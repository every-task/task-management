package com.playdata.task;

import com.playdata.client.chatgpt.service.ChatGptService;
import com.playdata.client.story.service.SuccessStoryClient;
import com.playdata.domain.articleindex.entity.ArticleIndex;
import com.playdata.domain.articleindex.repository.ArticleIndexRepository;
import com.playdata.domain.task.entity.TaskInformation;
import com.playdata.domain.task.repository.TaskInformationRepository;
import com.playdata.kafka.dto.ArticleKafkaData;
import com.playdata.kafka.dto.TaskKafkaData;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.playdata.domain.task.entity.Period.ALWAYS;
import static com.playdata.domain.task.entity.Period.DAILY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class TaskServiceTest {

    @Autowired
    private TaskInformationRepository taskInformationRepository;
    @Autowired
    private ArticleIndexRepository articleIndexRepository;
    @Autowired
    private SuccessStoryClient successStoryClient;
    @MockBean
    private ChatGptService chatGptService;
    @Autowired
    private TaskService taskService;
    @Autowired
    EntityManager entityManager;

    @BeforeEach
    public void setup() {
        // 인메모리 몽고 DB가 초기화가 되지 않아서 임시방편..
        articleIndexRepository.deleteAll();
    }


    @DisplayName("태스크 등록 시 태스크 정보가 저장되어야 한다.")
    @Test
    void saveTaskInformation(){
        //given
        TaskKafkaData task1 = new TaskKafkaData(UUID.randomUUID(), ALWAYS, "운동을 하세요");
        TaskKafkaData task2 = new TaskKafkaData(UUID.randomUUID(), DAILY, "잠을 자세요");

        ArticleKafkaData articleKafkaData = new ArticleKafkaData(
                1L,
                "살아가는 법",
                "건강하게 삶을 사는 법.",
                List.of(task1, task2));

        when(chatGptService.parseContent(anyString()))
                .thenReturn(anyList());

        //then
        taskService.taskRegister(articleKafkaData);

        //when
        List<TaskInformation> taskInformations = taskInformationRepository.findAll();

        assertThat(taskInformations).hasSize(2)
                .extracting("content", "period")
                .containsExactlyInAnyOrder(
                        tuple("운동을 하세요", ALWAYS),
                        tuple("잠을 자세요", DAILY)
                );
    }

    @DisplayName("태스크 등록 시 색인정보가 저장되어야 한다.")
    @Test
    void saveArticleIndex(){
        //given
        TaskKafkaData task1 = new TaskKafkaData(UUID.randomUUID(), ALWAYS, "운동을 하세요");
        TaskKafkaData task2 = new TaskKafkaData(UUID.randomUUID(), DAILY, "잠을 자세요");

        ArticleKafkaData articleKafkaData = new ArticleKafkaData(
                1L,
                "살아가는 법",
                "건강하게 삶을 사는 법.",
                List.of(task1, task2));

        when(chatGptService.parseContent(anyString()))
                .thenReturn(List.of("건강", "삶", "법"));

        //then
        taskService.taskRegister(articleKafkaData);

        //when
        List<ArticleIndex> articleIndices = articleIndexRepository.findAll();
        assertThat(articleIndices).hasSize(3);

        ArticleIndex index1 = articleIndexRepository.findById("건강").get();
        assertThat(index1.getTasks()).hasSize(2);
        ArticleIndex index2 = articleIndexRepository.findById("삶").get();
        assertThat(index2.getTasks()).hasSize(2);
        ArticleIndex index3 = articleIndexRepository.findById("법").get();
        assertThat(index3.getTasks()).hasSize(2);
    }

    @DisplayName("태스크 등록 시 이미 등록된 색인이 있으면, task id가 추가되어야 한다.")
    @Test
    void addArticleIndex(){
        //given
        ArticleIndex articleIndex = ArticleIndex.builder()
                .word("건강")
                .tasks(Set.of(UUID.randomUUID(), UUID.randomUUID()))
                .build();

        articleIndexRepository.save(articleIndex);

        TaskKafkaData task1 = new TaskKafkaData(UUID.randomUUID(), ALWAYS, "운동을 하세요");
        TaskKafkaData task2 = new TaskKafkaData(UUID.randomUUID(), DAILY, "잠을 자세요");

        ArticleKafkaData articleKafkaData = new ArticleKafkaData(
                1L,
                "살아가는 법",
                "건강하게 삶을 사는 법.",
                List.of(task1, task2));

        when(chatGptService.parseContent(anyString()))
                .thenReturn(List.of("건강", "삶", "법"));

        //then
        taskService.taskRegister(articleKafkaData);

        //when
        ArticleIndex index1 = articleIndexRepository.findById("건강").get();
        assertThat(index1.getTasks()).hasSize(4);

        ArticleIndex index2 = articleIndexRepository.findById("삶").get();
        assertThat(index2.getTasks()).hasSize(2);

        ArticleIndex index3 = articleIndexRepository.findById("법").get();
        assertThat(index3.getTasks()).hasSize(2);
    }
}