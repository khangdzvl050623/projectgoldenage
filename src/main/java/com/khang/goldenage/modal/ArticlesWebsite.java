package com.khang.goldenage.modal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ArticlesWebsite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String code;
    private String name;
    private String link;

    // Constructor rỗng
    public ArticlesWebsite() {
    }

    // Ghi đè phương thức toString
    @Override
    public String toString() {
        return "Website ID: " + id +
                "\nCode: " + code +
                "\nName: " + name +
                "\nLink: " + link;
    }
}

