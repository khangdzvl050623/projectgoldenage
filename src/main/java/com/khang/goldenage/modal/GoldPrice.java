package com.khang.goldenage.modal;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="goldprice")
public class GoldPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
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
    @Column(name = "fetchedTime")
    private Date fetchedTime;



}
