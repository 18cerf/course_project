package com.project.course_project.entity.user;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;


@Entity
@Table(name = "date_time")
public class DateTime {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Timestamp timestamp;

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp() {
        this.timestamp = Timestamp.valueOf(LocalDateTime.now());
    }
}
