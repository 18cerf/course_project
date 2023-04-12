package com.project.course_project.repository.user;


import com.project.course_project.entity.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    User findUserById(Long id);

    User findUserByUsername(String username);

    User findUserByLastname(String lastname);

    List<User> findAll();

    boolean existsUserByUsername(String username);

//    List<User> getFriendListByUserId(Long id);


}
