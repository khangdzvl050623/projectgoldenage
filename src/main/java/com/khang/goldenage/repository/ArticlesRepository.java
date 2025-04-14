package com.khang.goldenage.repository;

import com.khang.goldenage.modal.Articles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticlesRepository extends JpaRepository<Articles, Long> {
    boolean existsByLink(String link);
}
