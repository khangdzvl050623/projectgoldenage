package com.khang.goldenage.repository;

import com.khang.goldenage.modal.Articles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticlesRepository extends JpaRepository<Articles, Long> {
    List<Articles> findAllByOrderByDateTimeDesc();
    
    boolean existsByLink(String link);
}
