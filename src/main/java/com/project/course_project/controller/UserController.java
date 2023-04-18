package com.project.course_project.controller;

import com.project.course_project.entity.date.DateTime;
import com.project.course_project.entity.user.User;
import com.project.course_project.repository.date.DateRepository;
import com.project.course_project.repository.user.UserRepository;
import com.project.course_project.service.image.UserImageService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DateRepository dateRepository;
    private final UserImageService userImageService;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, DateRepository dateRepository, UserImageService userImageService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.dateRepository = dateRepository;
        this.userImageService = userImageService;
    }

    @GetMapping("/main")
    public String showMainUser(Model model, @AuthenticationPrincipal User user) {
        log.info(user.toString());
        //model.addAllAttributes(getUserMap(user));
        model.addAttribute("user", user);

        model.addAttribute("user_friends", user.getFriends());

        DateTime dateTime = new DateTime();
        dateTime.setTimestamp();
        user.getLoginTimes().add(dateTime);
        userRepository.save(user);

        Set<String> loginTimes = new TreeSet<String>(Comparator.reverseOrder());

        for (DateTime time : user.getLoginTimes()) {
            loginTimes.add(time.getTimestamp().toString());
        }

        /////

        byte[] imageData = userImageService.getUserImage(user.getId());
        if (imageData != null) {
            String base64ImageData = Base64.getEncoder().encodeToString(imageData);
            model.addAttribute("userImage", base64ImageData);
        }

        ///////
        model.addAttribute("login_times", loginTimes);
        return "main";
    }


    /////////////




    @PostMapping("/image")
    public String uploadUserImage(@RequestParam("file") MultipartFile image,
    @AuthenticationPrincipal User user) {
        if (!image.isEmpty()) {
            try {
                userImageService.saveUserImage(image.getBytes(), user);
            } catch (Exception e){
                log.info(e.toString());
            }
        }
        return "redirect:/users/main";
    }








    /////////


    @GetMapping("/main/delete/logs")
    public String deleteUsersLogs(@AuthenticationPrincipal User user) {
        user.getLoginTimes().clear();
        userRepository.save(user);
        return "redirect:/users/main";
    }

    @GetMapping("/main/edit")
    public String editMainUser(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "edit-main-user";
    }

    @PostMapping("/main/edit")
    public String saveMainEdit(@Valid User editedUser, BindingResult bindingResult,
                               @AuthenticationPrincipal User user) {
        if (bindingResult.hasErrors()) {
            return "edit-main-user";
        } else {
            user.setUsername(editedUser.getUsername());
            user.setPassword(passwordEncoder.encode(editedUser.getPassword()));
            user.setName(editedUser.getName());
            user.setLastname(editedUser.getLastname());
            user.setPhoneNumber(editedUser.getPhoneNumber());
            user.setEmail(editedUser.getEmail());

            userRepository.save(user);
            return "redirect:/users/main";
        }
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
            try {
//                user.setFriend(newFriend);
//                userRepository.save(user);
//                newFriend.setFriend(user);
//                userRepository.save(newFriend);
                user.getFriends().add(newFriend);
                userRepository.save(user);

                newFriend.getFriends().add(user);
                userRepository.save(newFriend);
            } catch (Exception e) {
                log.info(e.toString());
            }
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
