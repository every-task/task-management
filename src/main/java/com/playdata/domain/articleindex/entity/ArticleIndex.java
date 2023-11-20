package com.playdata.domain.articleindex.entity;

import com.playdata.domain.task.entity.ArticleCategory;
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
    private ArticleCategory category;
    private Set<UUID> tasks;

    public static ArticleIndex createArticleIndex(String word, ArticleCategory category, Set<UUID> tasks) {
        return ArticleIndex.builder()
                .word(word)
                .category(category)
                .tasks(tasks)
                .build();
    }

    @Builder
    public ArticleIndex(String word, ArticleCategory category, Set<UUID> tasks) {
        this.word = word;
        this.category = category;
        this.tasks = tasks;
    }
}
