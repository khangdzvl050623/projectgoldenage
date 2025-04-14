package com.khang.goldenage.controller;

import com.khang.goldenage.modal.Articles;
import com.khang.goldenage.modal.ExchangeRate;
import com.khang.goldenage.service.ArticlesService;
import com.khang.goldenage.service.ScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/scrape")
public class ArticlesController {
    @Autowired
    private ArticlesService articlesService;

    @GetMapping("/history")
    public ResponseEntity<List<Articles>> getArticlesHistory() {
        return ResponseEntity.ok(articlesService.getArticles());
    }


}
