package com.khang.goldenage.controller;

import com.khang.goldenage.modal.ExchangeRate;
import com.khang.goldenage.modal.GoldPrice;
import com.khang.goldenage.service.ExchangeRateService;
import com.khang.goldenage.service.GoldPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/exchange-rate")
public class ExchangeRateController {

    @Autowired
    private ExchangeRateService exchangeRateService;

    @GetMapping("/history")
    public ResponseEntity<List<ExchangeRate>> getExchangeRateHistory() {
        return ResponseEntity.ok(exchangeRateService.getExchangeRate());
    }

    @GetMapping("/current-exchange-rate")
    public ResponseEntity<List<ExchangeRate>> getCurrentExchangeRate() {
        return ResponseEntity.ok(exchangeRateService.getCurentExchangeRates());
    }


}
