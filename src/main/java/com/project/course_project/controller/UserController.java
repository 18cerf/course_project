package com.project.course_project.controller;

import com.project.course_project.entity.date.DateTime;
import com.project.course_project.entity.user.User;
import com.project.course_project.repository.date.DateRepository;
import com.project.course_project.repository.image.UserImageRepository;
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


/*
 * Контроллер, предназначенный для работы с информацией о пользователях
 */

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DateRepository dateRepository;
    private final UserImageService userImageService;
    private final UserImageRepository userImageRepository;

    /*
    Внедряем бины userRepository, passwordEncoder, dateRepository, dateRepository, userImageService, userImageRepository
     */
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, DateRepository dateRepository, UserImageService userImageService, UserImageRepository userImageRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.dateRepository = dateRepository;
        this.userImageService = userImageService;
        this.userImageRepository = userImageRepository;
    }

    /*
     * Метод заполняет модель данных о пользователе, его друзьях, логах входа и его фотографию
     */
    @GetMapping("/main")
    public String showMainUser(Model model, @AuthenticationPrincipal User user) {
//        log.info(user.toString());
        //model.addAllAttributes(getUserMap(user));
        model.addAttribute("user", userRepository.findUserById(user.getId()));

        model.addAttribute("user_friends", userRepository.findUserById(user.getId()).getFriends());

        DateTime dateTime = new DateTime();
        dateTime.setTimestamp();
        user.getLoginTimes().add(dateTime);
        userRepository.save(user);

        Set<String> loginTimes = new TreeSet<String>(Comparator.reverseOrder());


        /////

        byte[] imageData = userImageService.getUserImage(user.getId());
        if (imageData != null) {
            String base64ImageData = Base64.getEncoder().encodeToString(imageData);
            model.addAttribute("userImage", base64ImageData);
        }

        ///////


        for (DateTime time : user.getLoginTimes()) {
            loginTimes.add(time.getTimestamp().toString());
        }

        model.addAttribute("login_times", loginTimes);
        return "main";
    }


    /////////////


    /*
     * Метод принимает новое изображение пользователя, проверяет не пустой ли он, и затем сохраняет его в БД.
     */
    @PostMapping("/image")
    public String uploadUserImage(@RequestParam("file") MultipartFile image,
                                  @AuthenticationPrincipal User user) {
        if (!image.isEmpty()) {
            try {
                userImageRepository.delete(userImageRepository.findFirstByUserId(user.getId()).get());
            } catch (Exception e) {
                log.info(e.toString());
            }

            try {
                userImageService.saveUserImage(image.getBytes(), user);
            } catch (Exception e) {
                log.info(e.toString());
            }
        }
        return "redirect:/users/main";
    }


    /*
     * Метод удаляет данные пользователя о последних входах
     */

    @GetMapping("/main/delete/logs")
    public String deleteUsersLogs(@AuthenticationPrincipal User user) {
        user.getLoginTimes().clear();
        userRepository.save(user);
        return "redirect:/users/main";
    }


    /*
     * Метод добавляет в модель пользователя для удобной корректировки данных и затем возвращает представление "edit-main-user".
     */
    @GetMapping("/main/edit")
    public String editMainUser(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "edit-main-user";
    }

    /*
     * В случае успешной валидации данных происходит редирект на основную страницу
     *
     * Если данные валидацию не прошли, пользователь возвращается на страницу изменения данных.
     */
    @PostMapping("/main/edit")
    public String saveMainEdit(@Valid User editedUser, BindingResult bindingResult,
                               @AuthenticationPrincipal User user, Model model) {


        if (userRepository.existsUserByUsername(editedUser.getUsername())) {
            if (!editedUser.getUsername().equals(user.getUsername())) {
                model.addAttribute("error_message", "Пользователь с таким логином уже существует");
                return "edit-main-user";
            }
        }

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


    /*
     * Метод заполняет модель пользователями, не являющихся друзьями авторизованного пользователя
     */
    @GetMapping("/")
    public String allUsers(Model model, @AuthenticationPrincipal User user) {
//        model.addAttribute("users", userRepository.findAll());
        ArrayList<Long> friendsIds = new ArrayList<Long>();
        for (User friends :
                userRepository.findUserById(user.getId()).getFriends()) {
            friendsIds.add(friends.getId());
        }
        friendsIds.add(user.getId());
        model.addAttribute("users", userRepository.findUsersByIdNotIn(friendsIds));
        return "all_users";
    }

    /*
     * Метод позволяет добавить пользователя в друзья, и проверяет, не являются ли пользователи уже друзьями,
     * или не пытается ли пользователь добавить сам себя в друзья.
     */

    @PostMapping("/{id}")
    public String addFriend(@AuthenticationPrincipal User user, @PathVariable("id") Long newFriendId) {
        User newFriend = userRepository.findUserById(newFriendId);

        if (!(userRepository.findUserById(user.getId()).getFriends().contains(newFriend) || user.getId() == newFriendId || newFriend.getFriends().contains(user))) {
            try {
                user = userRepository.findUserById(user.getId());
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

    /*
     * Метод возвращает данные о всех пользователях, подходящих по параметру запроса.
     */
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

    /*
     * Метод позволяет получить данные о пользователе по id
     */
    @GetMapping("/{id}")
    public String getUser(@PathVariable("id") Long id, Model model) {

        byte[] imageData = userImageService.getUserImage(id);
        if (imageData != null) {
            String base64ImageData = Base64.getEncoder().encodeToString(imageData);
            model.addAttribute("userImage", base64ImageData);
        }

        Set<String> loginTimes = new TreeSet<String>(Comparator.reverseOrder());

        User user = userRepository.findById(id).get();
        for (DateTime time : user.getLoginTimes()) {
            loginTimes.add(time.getTimestamp().toString());
        }
        model.addAttribute("login_times", loginTimes);


        model.addAttribute("user", userRepository.findUserById(id));
        model.addAttribute("user_friends", userRepository.findUserById(id).getFriends());
        return "user";
    }


}
