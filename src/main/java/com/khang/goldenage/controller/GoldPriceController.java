package com.khang.goldenage.controller;

import com.khang.goldenage.modal.GoldPrice;
import com.khang.goldenage.service.CronGetGoldAndExchangeRateService;
import com.khang.goldenage.service.GoldPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/gold-prices")
public class GoldPriceController {


    @Autowired
    private GoldPriceService goldPriceService;

    @GetMapping("/history")
    public ResponseEntity<List<GoldPrice>> getGoldPriceHistory() {
        return ResponseEntity.ok(goldPriceService.getGoldPrices());
    }

    @GetMapping("/current-gold-prices")
    public ResponseEntity<List<GoldPrice>> getCurrentGoldPrices() {
        return ResponseEntity.ok(goldPriceService.getCurrentGoldPrices());
    }


}
