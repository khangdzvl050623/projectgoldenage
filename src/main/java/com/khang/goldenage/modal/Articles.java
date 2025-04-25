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
    private String mediaUrl;
    private String time;
    private LocalDateTime dateTime;
    private String mediaType;

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
                ", mediaUrl='" + mediaUrl + '\'' +
                ", time='" + time + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", mediaType='" + mediaType + '\'' +
                '}';
    }
}
