package com.khang.goldenage.service;

import com.khang.goldenage.modal.ExchangeRate;
import com.khang.goldenage.modal.GoldPrice;
import com.khang.goldenage.repository.GoldPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

import java.util.List;

@Service
public class CronGetGoldAndExchangeRateService {
  @Autowired
  private GoldPriceService goldPriceService;
  @Autowired
  private ExchangeRateService exchangeRateService;


  @Scheduled(fixedDelay = 3600000)
  public void fetchGoldPrice() throws IOException {
    String url = "http://api.btmc.vn/api/BTMCAPI/getpricebtmc?key=3kd8ub1llcg9t45hnoh8hmn7t5kc2v";
    try {

      List<GoldPrice> goldPrices = goldPriceService.parsePriceFromUrl(url);

      // display ra
      goldPrices.forEach(goldPrice -> System.out.println(goldPrice));


    } catch (Exception e) {
      System.err.println("Error fetching gold prices: " + e.getMessage());
    }
  }

  @Scheduled(fixedDelay = 3600000)
  public void fetchExchangeRate() {
    String url = "https://portal.vietcombank.com.vn/Usercontrols/TVPortal.TyGia/pXML.aspx?b=8";
    try {
      System.out.println("Bắt đầu lấy tỷ giá...");
      String response = exchangeRateService.fetchPriceFromUrl(url); // Phương thức mới chỉ lấy dữ liệu thô
      System.out.println("Dữ liệu nhận được: " + response); // Ghi lại dữ liệu nhận được

      List<ExchangeRate> exchangeRates = exchangeRateService.parseExchangeRateFromUrl(url);

      if (exchangeRates.isEmpty()) {
        System.out.println("Không có tỷ giá nào được lấy.");
      } else {
        exchangeRates.forEach(System.out::println);
        System.out.println("Tỷ giá đã được lấy và lưu thành công.");
      }
    } catch (Exception e) {
      System.err.println("Lỗi khi lấy tỷ giá: " + e.getMessage());
      e.printStackTrace(); // Ghi lại toàn bộ stack trace để khắc phục lỗi
    }
  }


}
