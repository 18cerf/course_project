package com.project.course_project.repository.user;


import com.project.course_project.entity.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    User findUserById(Long id);

    User findUserByUsername(String username);

    User findUserByLastname(String lastname);

    List<User> findAll();

    boolean existsUserByUsername(String username);

    List<User> findUsersByIdNotIn(List<Long> users);

    List<User> findByUsernameContaining(String partialNickname);

    List<User> findByNameContaining(String partialNickname);

    List<User> findByLastnameContaining(String partialNickname);

    List<User> findByPhoneNumberContaining(String partialNickname);

//    List<User> findAllFriendsByUserId(Long id);


}
