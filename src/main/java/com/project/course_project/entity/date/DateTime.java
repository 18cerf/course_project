package com.project.course_project.entity.date;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;


/*
 * Entity-класс, предназначенный для хранения данных о входах пользователей в приложение
 */
@Entity
@Table(name = "date_time")
public class DateTime {

    /*
     * Id сущности
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Поле времени
     */
    private Timestamp timestamp;

    /*
     * Геттер для получения времени
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /*
     * Сеттер для сохранения времени в нужном формате с помощью класса LocalDateTime
     */
    public void setTimestamp() {
        this.timestamp = Timestamp.valueOf(LocalDateTime.now());
    }
}
