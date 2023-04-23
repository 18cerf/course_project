package com.project.course_project.repository.image;

import com.project.course_project.entity.image.UserImage;
import com.project.course_project.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


/*
 *
 * Интерфейс для работы в БД с сущностью UserImage
 *
 */
public interface UserImageRepository extends JpaRepository<UserImage, Long> {

    /*
     *
     * Возвращает фото пользователя
     *
     */
    Optional<UserImage> findFirstByUserId(Long userId);

    /*
     *
     * Удаляет все фото по id изображения
     *
     */
    void deleteById(Long id);


    /*
     *
     * Удаляет все фото по id пользователя
     *
     */
    void deleteAllByUserId(Long userId);
}