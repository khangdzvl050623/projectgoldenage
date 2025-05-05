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

  @Query("""
    SELECT g FROM GoldPrice g
    WHERE g.updatedTime = (
        SELECT MAX(g2.updatedTime)
        FROM GoldPrice g2
        WHERE g2.goldType = g.goldType
    )
""")
  List<GoldPrice> findLatestGoldPricesByUpdatedTime();

}
