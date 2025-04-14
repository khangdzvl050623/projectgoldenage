package com.khang.goldenage.service;

import com.khang.goldenage.modal.Articles;
import com.khang.goldenage.repository.ArticlesRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScraperService {
    @Autowired
    ArticlesRepository articlesRepository;
    public List<Articles> scrapeArticles(String url) {
        List<Articles> articlesList = new ArrayList<>();
        try {
            // Kết nối và lấy HTML của trang
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .get();

            // Kiểm tra xem trang có được tải thành công không
            System.out.println("Trang web: " + doc.title());

            // Lấy tất cả các bài viết
            Elements articles = doc.select("article.item-news.item-news-common.thumb-left");
            System.out.println("Tìm thấy số bài viết: " + articles.size());

            // Lặp qua từng bài viết và thu thập dữ liệu
            for (Element article : articles) {
                String link = article.select("h3.title-news a").attr("href");
                String title = article.select("h3.title-news a").text();
                String description = article.select("p.description").text();
                String imageUrl = article.select("div.thumb-art img").attr("src");
                String timeAgo = article.select("span.time-ago").text();

                Articles newArticle = new Articles();
                newArticle.setLink(link);
                newArticle.setTitle(title);
                newArticle.setDescription(description);
                newArticle.setImageUrl(imageUrl);
                newArticle.setTime(timeAgo);

                articlesList.add(newArticle);
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi scrape dữ liệu từ URL: " + e.getMessage());
            e.printStackTrace();
        }
        return articlesList;
    }

    public void saveArticles(List<Articles> articlesList) {
        articlesList.forEach(article -> {
            try {
                if (!articlesRepository.existsByLink(article.getLink())) {
                    articlesRepository.save(article); // Lưu bài viết mới
                    System.out.println("Đã lưu bài viết: " + article.getTitle());
                } else {
                    System.out.println("Bài viết đã tồn tại: " + article.getTitle());
                }
            } catch (Exception e) {
                System.err.println("Lỗi khi lưu bài viết: " + article.getTitle());
                e.printStackTrace();
            }
        });
    }
}
