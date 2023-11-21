package com.playdata.task;

import com.playdata.client.chatgpt.exception.ChatGptException;
import com.playdata.client.chatgpt.exception.ChatGptExceptionType;
import com.playdata.client.chatgpt.service.ChatGptService;
import com.playdata.domain.articleindex.entity.ArticleIndex;
import com.playdata.domain.articleindex.repository.ArticleIndexRepository;
import com.playdata.domain.task.entity.ArticleCategory;
import com.playdata.kafka.dto.ArticleKafkaData;
import com.playdata.kafka.dto.TaskKafkaData;
import com.playdata.kafka.producer.StoryProducer;
import com.playdata.task.service.TaskService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.playdata.domain.task.entity.Period.ALWAYS;
import static com.playdata.domain.task.entity.Period.DAILY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class TaskServiceTest {
    @Autowired
    private ArticleIndexRepository articleIndexRepository;
    @MockBean
    private ChatGptService chatGptService;
    @Autowired
    private TaskService taskService;
    @MockBean
    private StoryProducer storyProducer;

    @AfterEach
    public void clear() {
        articleIndexRepository.deleteAll();
    }

    @DisplayName("태스크 등록 시 색인정보가 저장되어야 한다.")
    @Test
    void saveArticleIndex(){
        //given
        TaskKafkaData task1 = new TaskKafkaData(UUID.randomUUID(), ALWAYS, "운동을 하세요");
        TaskKafkaData task2 = new TaskKafkaData(UUID.randomUUID(), DAILY, "잠을 자세요");

        ArticleKafkaData articleKafkaData = ArticleKafkaData.builder()
                .id(1L)
                .title("살아가는 법")
                .content("건강하게 삶을 사는 법.")
                .category(ArticleCategory.HEALTH)
                .tasks(List.of(task1, task2))
                .build();


        when(chatGptService.parseContent(anyString()))
                .thenReturn(CompletableFuture.completedFuture(List.of("건강", "삶", "법")));

        //when
        taskService.taskRegister(articleKafkaData);

        //then
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
                .category(ArticleCategory.HEALTH)
                .build();

        articleIndexRepository.save(articleIndex);

        TaskKafkaData task1 = new TaskKafkaData(UUID.randomUUID(), ALWAYS, "운동을 하세요");
        TaskKafkaData task2 = new TaskKafkaData(UUID.randomUUID(), DAILY, "잠을 자세요");

        ArticleKafkaData articleKafkaData = ArticleKafkaData.builder()
                .id(1L)
                .title("살아가는 법")
                .content("건강하게 삶을 사는 법.")
                .category(ArticleCategory.HEALTH)
                .tasks(List.of(task1, task2))
                .build();

        when(chatGptService.parseContent(anyString()))
                .thenReturn(CompletableFuture.completedFuture(List.of("건강", "삶", "법")));


        //when
        taskService.taskRegister(articleKafkaData);

        //then
        ArticleIndex index1 = articleIndexRepository.findByWordAndCategory("건강", ArticleCategory.HEALTH).get();
        assertThat(index1.getTasks()).hasSize(4);

        ArticleIndex index2 = articleIndexRepository.findByWordAndCategory("삶", ArticleCategory.HEALTH).get();
        assertThat(index2.getTasks()).hasSize(2);

        ArticleIndex index3 = articleIndexRepository.findByWordAndCategory("법", ArticleCategory.HEALTH).get();
        assertThat(index3.getTasks()).hasSize(2);
    }

    @DisplayName("chat gpt 사용 실패 시, 실패 내역이 전송되어야 한다.")
    @Test
    void executeRecoverWhenFailChatGptCompletion(){
        //given
        ArticleKafkaData articleKafkaData = ArticleKafkaData.builder()
                .id(1L)
                .title("살아가는 법")
                .content("건강하게 삶을 사는 법.")
                .category(ArticleCategory.HEALTH)
                .tasks(List.of())
                .build();

        when(chatGptService.parseContent(anyString()))
                .thenThrow(new ChatGptException(ChatGptExceptionType.CHAT_GPT_COMPLETION_FAIL));

        //when
        taskService.taskRegister(articleKafkaData);

        //then
        verify(storyProducer).send(anyLong());
    }
}