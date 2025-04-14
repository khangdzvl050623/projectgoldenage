package com.khang.goldenage.modal;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="goldprice")
public class GoldPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    @Column(name = "goldName")
    private String goldName;
    @Column(name = "goldType")
    private String goldType;
    @Column(name = "purchasePrice")
    private Integer purchasePrice;
    @Column(name = "sellPrice")
    private Integer sellPrice;
    @Column(name = "updatedTime")
    private Date updatedTime;

    public String getGoldName() {
        return goldName;
    }

    public void setGoldName(String goldName) {
        this.goldName = goldName;
    }

    public String getGoldType() {
        return goldType;
    }

    public void setGoldType(String goldType) {
        this.goldType = goldType;
    }

    public Integer getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Integer purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Integer getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Integer sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public String toString() {
        return "GoldPrice [id=" + id + ", goldName=" + goldName + ", goldType="+goldType +", purchasePrice="+purchasePrice+", sellPrice="+sellPrice+", updatedTime="+updatedTime+"]";
    }


}
