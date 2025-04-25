package com.khang.goldenage.service;

import com.khang.goldenage.modal.Articles;
import com.khang.goldenage.repository.ArticlesRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScraperService {
    @Autowired
    ArticlesRepository articlesRepository;

    private String normalizeMediaUrl(String mediaUrl) {
        if (mediaUrl == null || mediaUrl.isEmpty()) {
            return null;
        }

        // Nếu là base64, bỏ qua
        if (mediaUrl.startsWith("data:")) {
            return null;
        }

        // Nếu là URL đầy đủ (bao gồm cả vncdn.net và vnecdn.net)
        if (mediaUrl.startsWith("http")) {
            return mediaUrl;  // Trả về trực tiếp URL đầy đủ
        }

        // Nếu là URL tương đối
        if (mediaUrl.startsWith("/")) {
            return "https://vnexpress.net" + mediaUrl;
        }

        return null;
    }

    public List<Articles> scrapeArticles(String url) {
        List<Articles> articlesList = new ArrayList<>();
        try {
            System.out.println("=== BẮT ĐẦU SCRAPING ===");
            System.out.println("URL: " + url);

            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .get();

            System.out.println("Trang web: " + doc.title());
            Elements articles = doc.select("article.item-news.item-news-common.thumb-left");
            System.out.println("Số bài viết tìm thấy: " + articles.size());

            for (Element article : articles) {
                String link = article.select("h3.title-news a").attr("href");
                String title = article.select("h3.title-news a").text();
                String description = article.select("p.description").text();
                
                // Xử lý media (video hoặc hình ảnh)
                Element thumbArt = article.select("div.thumb-art").first();
                String mediaUrl = "";
                String mediaType = "image";
                
                System.out.println("\n=== DEBUG MEDIA ===");
                System.out.println("Đang xử lý bài: " + title);
                
                if (thumbArt != null) {
                    System.out.println("Tìm thấy thumb-art");
                    
                    // Kiểm tra video trước
                    Element video = thumbArt.select("video").first();
                    if (video != null) {
                        System.out.println("Tìm thấy video");
                        Element source = video.select("source").first();
                        if (source != null) {
                            System.out.println("Tìm thấy source");
                            // Ưu tiên lấy data-src-video trước
                            mediaUrl = source.attr("data-src-video");
                            System.out.println("data-src-video: " + mediaUrl);
                            
                            if (mediaUrl.isEmpty()) {
                                mediaUrl = source.attr("src");
                                System.out.println("src: " + mediaUrl);
                            }
                            mediaType = "video";
                        }
                    } else {
                        System.out.println("Không tìm thấy video, kiểm tra hình ảnh");
                        // Nếu không có video, kiểm tra picture và img
                        Element picture = thumbArt.select("picture").first();
                        if (picture != null) {
                            System.out.println("Tìm thấy picture");
                            Element img = picture.select("img").first();
                            if (img != null) {
                                System.out.println("Tìm thấy img");
                                mediaUrl = img.attr("data-src");
                                System.out.println("data-src: " + mediaUrl);
                                
                                if (mediaUrl.isEmpty()) {
                                    mediaUrl = img.attr("src");
                                    System.out.println("src: " + mediaUrl);
                                }
                                mediaType = "image";
                            }
                        }
                    }
                }

                System.out.println("Media URL trước khi normalize: " + mediaUrl);
                String normalizedMediaUrl = normalizeMediaUrl(mediaUrl);
                System.out.println("Media URL sau khi normalize: " + normalizedMediaUrl);

                String timeAgo = article.select("span.time-ago").text();
                LocalDateTime now = LocalDateTime.now();

                System.out.println("\n=== BÀI VIẾT MỚI ===");
                System.out.println("Tiêu đề: " + title);
                System.out.println("Link: " + link);
                System.out.println("Mô tả: " + description);
                System.out.println("Loại media: " + mediaType);
                System.out.println("URL media: " + normalizedMediaUrl);
                System.out.println("Thời gian đăng: " + timeAgo);
                System.out.println("Thời gian crawl: " + now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));

                Articles newArticle = new Articles();
                newArticle.setLink(link);
                newArticle.setTitle(title);
                newArticle.setDescription(description);
                newArticle.setMediaUrl(normalizedMediaUrl);
                newArticle.setMediaType(mediaType);
                newArticle.setTime(timeAgo);
                newArticle.setDateTime(now);

                articlesList.add(newArticle);
            }

            System.out.println("\n=== KẾT THÚC SCRAPING ===");
            System.out.println("Tổng số bài viết đã crawl: " + articlesList.size());

        } catch (Exception e) {
            System.err.println("\n=== LỖI SCRAPING ===");
            System.err.println("URL gây lỗi: " + url);
            System.err.println("Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
        return articlesList;
    }

    public void saveArticles(List<Articles> articlesList) {
        System.out.println("\n=== BẮT ĐẦU LƯU DỮ LIỆU ===");
        int savedCount = 0;
        int existingCount = 0;
        int errorCount = 0;

        for (Articles article : articlesList) {
            try {
                if (!articlesRepository.existsByLink(article.getLink())) {
                    articlesRepository.save(article);
                    savedCount++;
                    System.out.println("Đã lưu: " + article.getTitle());
                } else {
                    existingCount++;
                    System.out.println("Đã tồn tại: " + article.getTitle());
                }
            } catch (Exception e) {
                errorCount++;
                System.err.println("Lỗi khi lưu: " + article.getTitle());
                System.err.println("Chi tiết lỗi: " + e.getMessage());
            }
        }

        System.out.println("\n=== THỐNG KÊ LƯU DỮ LIỆU ===");
        System.out.println("Tổng số bài viết xử lý: " + articlesList.size());
        System.out.println("Số bài đã lưu mới: " + savedCount);
        System.out.println("Số bài đã tồn tại: " + existingCount);
        System.out.println("Số bài lỗi: " + errorCount);
    }
}
