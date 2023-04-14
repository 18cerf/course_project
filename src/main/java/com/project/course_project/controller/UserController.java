package com.project.course_project.controller;

import com.project.course_project.entity.user.User;
import com.project.course_project.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {

    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/main")
    public String home(Model model, @AuthenticationPrincipal User user) {
        log.info(user.toString());
        //model.addAllAttributes(getUserMap(user));
        model.addAttribute("user", user);

        model.addAttribute("user_friends", user.getFriends());

        return "main";
    }

    @GetMapping("/")
    public String allUsers(Model model, @AuthenticationPrincipal User user) {
//        model.addAttribute("users", userRepository.findAll());
        ArrayList<Long> friendsIds = new ArrayList<Long>();
        for (User friends :
                user.getFriends()) {
            friendsIds.add(friends.getId());
        }
        friendsIds.add(user.getId());
        model.addAttribute("users", userRepository.findUsersByIdNotIn(friendsIds));
        return "all_users";
    }

    @PostMapping("/{id}")
    public String addFriend(@AuthenticationPrincipal User user, @PathVariable("id") Long newFriendId) {
        User newFriend = userRepository.findUserById(newFriendId);

        if (!(user.getFriends().contains(newFriend) || user.getId() == newFriendId || newFriend.getFriends().contains(user))) {
            user.setFriend(newFriend);
            userRepository.save(user);
        }
        return "redirect:";
    }


    @GetMapping("/search")
    public String getUsersByParam(@RequestParam("searchParam") String searchParam, Model model) {
        Set<User> users = new HashSet();


        users.addAll(userRepository.findByUsernameContaining(searchParam));
        users.addAll(userRepository.findByLastnameContaining(searchParam));
        users.addAll(userRepository.findByNameContaining(searchParam));
        users.addAll(userRepository.findByPhoneNumberContaining(searchParam));

        if (users.isEmpty()) {
            model.addAttribute("message", "Нет пользователей удовлетворяющих запросу");
        }

        model.addAttribute("users", users);


        return "all_users";
    }


    @GetMapping("/{id}")
    public String getUser(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userRepository.findUserById(id));
        model.addAttribute("user_friends", userRepository.findUserById(id).getFriends());
        return "user";
    }


}
