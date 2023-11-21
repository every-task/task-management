package com.playdata.domain.articleindex.repository;

import com.playdata.domain.articleindex.entity.ArticleIndex;
import com.playdata.domain.task.entity.ArticleCategory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleIndexRepository extends MongoRepository<ArticleIndex, String> {

    List<ArticleIndex> findByWordInAndCategory(List<String> words, ArticleCategory category);
    Optional<ArticleIndex> findByWordAndCategory(String word, ArticleCategory category);
}
