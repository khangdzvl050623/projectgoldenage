package com.khang.goldenage.service;

import com.khang.goldenage.modal.ExchangeRate;
import com.khang.goldenage.modal.GoldPrice;
import com.khang.goldenage.modal.User;
import com.khang.goldenage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CronNotifyUserMailService {

    @Autowired
    private SendMailService sendMailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoldPriceService goldPriceService;

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Scheduled(cron = "0 0 8 * * ?") // Chạy mỗi ngày lúc 8 giờ sáng
    public void sendDailyGoldPriceNotifications() {
        List<User> users = userRepository.findAll();
        List<GoldPrice> goldPrices = goldPriceService.getLatestGoldPrices();

        if (goldPrices.isEmpty()) {
            System.out.println("Không có giá vàng để gửi thông báo hôm nay.");
            return;
        }

        String emailContent = generateEmailContentforGold(goldPrices);

        for (User user : users) {
            sendMailService.sendEmail(user.getEmail(), "Thông báo giá vàng hằng ngày", emailContent);
        }
        System.out.println("Đã gửi thông báo giá vàng cho tất cả người dùng.");
    }

    @Scheduled(cron = "0 0 8 * * ?") // Chạy mỗi ngày lúc 8 giờ sáng
    public void sendDailyExchangeRateNotifications() {
        List<User> users = userRepository.findAll();
        List<com.khang.goldenage.modal.ExchangeRate> exchangeRates = exchangeRateService.getCurentExchangeRates();

        if (exchangeRates.isEmpty()) {
            System.out.println("Không có giá vàng để gửi thông báo hôm nay.");
            return;
        }

        String emailContent = generateEmailContentForExchangeRate(exchangeRates);

        for (User user : users) {
            sendMailService.sendEmail(user.getEmail(), "Thông báo tỉ giá hằng ngày", emailContent);
        }
        System.out.println("Đã gửi thông báo giá vàng và gia tiền cho tất cả người dùng.");
    }

    private String generateEmailContentforGold(List<GoldPrice> goldPrices) {
        StringBuilder content = new StringBuilder("Giá vàng cập nhật hôm nay:\n\n");
        for (GoldPrice price : goldPrices) {
            content.append(String.format("Loại: %s\nMua: %d\nBán: %d\nCập nhật: %s\n\n", // string.fomat dinh dang chuoi tu tham so
                    price.getGoldName(), price.getPurchasePrice(), price.getSellPrice(), price.getUpdatedTime()));
        }
        return content.toString(); // chuyen doi string builder thanh string

    }
    private String generateEmailContentForExchangeRate(List<ExchangeRate> exchangeRates) {
        StringBuilder content = new StringBuilder("tỉ giá doi thoái cập nhật hôm nay:\n\n");
        for (ExchangeRate price : exchangeRates) {
            content.append(String.format("Mã: %s\nLoại: %s\nMua: %f\nTransfer:%f\nBán:%f\nUpdateTime:%s \n", // string.fomat dinh dang chuoi tu tham so
                   price.getCurrencyCode(),price.getCurrencyName(),price.getBuyRate(),price.getTransferRate(),price.getSellRate(),price.getUpdatedTime()));
        }
        return content.toString(); // chuyen doi string builder thanh string

    }

}
