package com.project.course_project.entity.image;


import com.project.course_project.entity.user.User;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

/*
 * Entity-класс, предназначенный для хранения фотографий пользователей
 */
@Entity
@Table(name = "user_image")
public class UserImage {

    /*
     * Id сущности
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Связываем таблицы аннотацией @OneToOne, джойним к колонке "user_id"
     */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    /*
     *  Lob аннотация позволяет сохранять картинки больших размеров
     */
    @Lob
    private byte[] imageData;

    /*
     * Возвращает пользователя, которому эта картинка принадлежит
     */
    public User getUser() {
        return user;
    }

    /*
     * Сохраняет пользователя, которому эта картинка принадлежит
     */
    public void setUser(User user) {
        this.user = user;
    }

    /*
     * Возвращает картинку в массиве byte
     */
    public byte[] getImageData() {
        return imageData;
    }

    /*
     * Сеттер для сохранения картинки в массиве byte
     */
    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    /*
     * Возвращает id
     */
    public Long getId() {
        return id;
    }

}
