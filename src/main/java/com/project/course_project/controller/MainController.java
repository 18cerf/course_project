package com.project.course_project.controller;

import com.project.course_project.entity.user.User;
import com.project.course_project.repository.user.UserRepository;
import jdk.dynalink.linker.LinkerServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class MainController {

    private UserRepository userRepository;

    public MainController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/main")
    public String home(Model model, @AuthenticationPrincipal User user) {
        log.info(user.toString());
        //model.addAllAttributes(getUserMap(user));
        model.addAttribute("user", user);

        return "main";
    }

    @GetMapping("/users")
    public String allusers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "users";
    }


}
