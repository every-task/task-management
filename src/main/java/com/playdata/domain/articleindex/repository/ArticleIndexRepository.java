package com.playdata.domain.articleindex.repository;

import com.playdata.domain.articleindex.entity.ArticleIndex;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ArticleIndexRepository extends MongoRepository<ArticleIndex, String> {

    List<ArticleIndex> findByWordIn(List<String> words);
}
