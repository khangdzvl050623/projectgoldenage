package com.khang.goldenage.service;

import com.khang.goldenage.modal.Articles;
import com.khang.goldenage.repository.ArticlesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticlesService {
    @Autowired
    private ArticlesRepository articlesRepository;
    public List<Articles> getArticles() {
        return articlesRepository.findAllByOrderByDateTimeDesc();
    }
}
