package com.playdata.domain.suggest.repository;

import com.playdata.domain.suggest.entity.Suggest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SuggestRepository extends JpaRepository<Suggest, UUID> {
}
