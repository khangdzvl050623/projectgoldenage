package com.khang.goldenage.modal;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "exchangerate")
@Data
@NoArgsConstructor
@ToString
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "currency_code", length = 3, nullable = false)
    private String currencyCode;

    @Column(name = "currency_name", length = 100)
    private String currencyName;

    @Column(name = "buy_rate", precision = 15, scale = 2)
    private BigDecimal buyRate;

    @Column(name = "transfer_rate", precision = 15, scale = 2)
    private BigDecimal transferRate;

    @Column(name = "sell_rate", precision = 15, scale = 2)
    private BigDecimal sellRate;

    @Column(name = "updated_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedTime;

    public ExchangeRate(String currencyCode, String currencyName, BigDecimal buyRate, BigDecimal transferRate, BigDecimal sellRate, Date updatedTime) {
        this.currencyCode = currencyCode;
        this.currencyName = currencyName;
        this.buyRate = buyRate;
        this.transferRate = transferRate;
        this.sellRate = sellRate;
        this.updatedTime = updatedTime;
    }
}
