package com.playdata.domain.suggest.repository;

import com.playdata.domain.suggest.entity.Suggest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SuggestRepository extends JpaRepository<Suggest, Long> {

    /*
    select s from Suggest s
    where s.id = ?
     */
    List<Suggest> findByQuestionId(Long id);
}
