package com.khang.goldenage.repository;

import com.khang.goldenage.modal.GoldPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface GoldPriceRepository extends JpaRepository<GoldPrice, Long> {


    // @Query("SELECT g FROM GoldPrice g WHERE g.updatedTime = (SELECT MAX(g.updatedTime) FROM GoldPrice g)")
    // List<GoldPrice> findGoldPricesByLatestDate();
    @Query(value = """
    SELECT *
    FROM (
        SELECT *, ROW_NUMBER() OVER (PARTITION BY gold_type ORDER BY updated_time DESC) as rn
        FROM gold_price
    ) sub
    WHERE rn = 1
    """, nativeQuery = true)
    List<GoldPrice> findLatestGoldPricesByGoldType();
}
