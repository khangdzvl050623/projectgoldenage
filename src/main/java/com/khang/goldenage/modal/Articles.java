package com.khang.goldenage.modal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class Articles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String link;
    private String description;
    private String imageUrl;
    private String time;
    private LocalDateTime dateTime;

    // Constructor rỗng
    public Articles() {
        this.dateTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Articles{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", time='" + time + '\'' +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }
}
