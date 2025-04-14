package com.khang.goldenage.repository;

import com.khang.goldenage.modal.GoldPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangeRateRepository  extends JpaRepository<com.khang.goldenage.modal.ExchangeRate, Long> {
    @Query("SELECT g FROM ExchangeRate g WHERE g.updatedTime = (SELECT MAX(g.updatedTime) FROM ExchangeRate g)")
    List<com.khang.goldenage.modal.ExchangeRate> findExchangeRateByLatestDate();

}
