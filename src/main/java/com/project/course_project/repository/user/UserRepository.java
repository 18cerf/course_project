package com.project.course_project.repository.user;


import com.project.course_project.entity.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;
/*
 *
 * Интерфейс для работы в БД с сущностью DateTime
 *
 */
public interface UserRepository extends CrudRepository<User, Long> {

    /*
     *
     * Возвращает фото пользователя
     *
     */
    User findUserById(Long id);

    /*
     *
     * Возвращает фото пользователя
     *
     */
    User findUserByUsername(String username);

    /*
     *
     * Возвращает лист всех пользователей
     *
     */
    List<User> findAll();

    /*
     *
     * Возвращает boolean-значение, проверяет, есть ли такой username в БД
     *
     */
    boolean existsUserByUsername(String username);

    /*
     *
     * Возвращает лист всех пользователей, кроме тех, чьи Id указаны в List
     *
     */
    List<User> findUsersByIdNotIn(List<Long> users);


    /*
     *
     * Возвращает лист всех пользователей, если они содержат указанный String
     *
     */
    List<User> findByUsernameContaining(String partialNickname);


    /*
     *
     * Возвращает лист всех пользователей, если они содержат указанный String
     *
     */
    List<User> findByNameContaining(String partialNickname);

    /*
     *
     * Возвращает лист всех пользователей, если они содержат указанный String
     *
     */
    List<User> findByLastnameContaining(String partialNickname);

    /*
     *
     * Возвращает лист всех пользователей, если они содержат указанный String
     *
     */
    List<User> findByPhoneNumberContaining(String partialNickname);

}
