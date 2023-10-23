package com.playdata.domain.articleindex.repository;

import com.playdata.domain.articleindex.entity.ArticleIndex;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArticleIndexRepository extends MongoRepository<ArticleIndex, String> {
}
