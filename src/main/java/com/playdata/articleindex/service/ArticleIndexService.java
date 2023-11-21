package com.playdata.articleindex.service;

import com.playdata.domain.articleindex.entity.ArticleIndex;
import com.playdata.domain.articleindex.repository.ArticleIndexRepository;
import com.playdata.domain.task.entity.ArticleCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleIndexService {

    private final ArticleIndexRepository articleIndexRepository;

    public List<UUID> getRelatedTaskIds(List<String> words, ArticleCategory category, int count){
        List<ArticleIndex> articleIndices = articleIndexRepository.findByWordInAndCategory(words, category);

        List<UUID> taskIds = articleIndices.stream()
                .map(ArticleIndex::getTasks)
                .flatMap(Set::stream)
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));

        return randomTaskIdsByCount(taskIds, count);
    }

    private List<UUID> randomTaskIdsByCount(List<UUID> taskIds, int count){
        if(taskIds.size()<=count){
            return taskIds;
        }

        Collections.shuffle(taskIds);

        return taskIds.subList(0, count);
    }
}
