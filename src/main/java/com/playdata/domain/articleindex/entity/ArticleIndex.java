package com.playdata.domain.articleindex.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.util.UUID;

@Document(collection = "article_index")
@Getter
@NoArgsConstructor
public class ArticleIndex {

    @Id
    private String word;
    private Set<UUID> tasks;

    public static ArticleIndex createArticleIndex(String word, Set<UUID> tasks) {
        return ArticleIndex.builder()
                .word(word)
                .tasks(tasks)
                .build();
    }

    @Builder
    public ArticleIndex(String word, Set<UUID> tasks) {
        this.word = word;
        this.tasks = tasks;
    }
}
