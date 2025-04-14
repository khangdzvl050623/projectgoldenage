package com.khang.goldenage.controller;

import com.khang.goldenage.service.CronNotifyUserMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {
    @Autowired
    private CronNotifyUserMailService cronNotifyUserMailService;

    @GetMapping("/test-send-email")
    public String testSendEmail() {
        cronNotifyUserMailService.sendDailyGoldPriceNotifications();
        cronNotifyUserMailService.sendDailyExchangeRateNotifications();
        return "Email notifications sent!";
    }

}
