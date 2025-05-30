package com.khang.goldenage.service;

import com.khang.goldenage.modal.Articles;
import com.khang.goldenage.repository.ArticlesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public class CronArticles {
    @Autowired
    private ScraperService scraperService;

    @Scheduled(fixedDelay = 86400000) // Chạy mỗi giờ
    public void fetchArticles() {
        String url = "https://vnexpress.net/tin-tuc-24h"; // URL cần lấy bài viết
        System.out.println("Bắt đầu cron lấy bài viết...");

        try {
            // Lấy danh sách bài viết từ service
            List<Articles> articlesList = scraperService.scrapeArticles(url);

            if (articlesList.isEmpty()) {
                System.out.println("Không có bài viết nào được lấy.");
                return;
            }

            // Lưu danh sách bài viết vào cơ sở dữ liệu
            scraperService.saveArticles(articlesList);

            System.out.println("Cron lấy bài viết hoàn tất. Số bài viết xử lý: " + articlesList.size());
        } catch (Exception e) {
            System.err.println("Lỗi khi chạy cron lấy bài viết: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
