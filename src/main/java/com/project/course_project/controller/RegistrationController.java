package com.project.course_project.controller;

import com.project.course_project.repository.user.UserRepository;
import com.project.course_project.forms.RegistrationForm;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
@Slf4j
public class RegistrationController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public RegistrationController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @ModelAttribute(name = "registerForm")
    public RegistrationForm registrationForm() {
        return new RegistrationForm();
    }

    @GetMapping
    public String registerForm() {
        return "registration";
    }

    @PostMapping
    public String processRegistration(@ModelAttribute("registerForm") @Valid RegistrationForm registrationForm,
                                      Errors errors,
                                      Model model,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("Неудачная попытка валидации с данными: {}", registrationForm.toString());
            model.addAttribute("message", "Ошибка валидации данных, проверьте правильность вводимых полей");
            return "registration";
        }

        if (!userRepository.existsUserByUsername(registrationForm.getUsername())) {
            try {
                userRepository.save(registrationForm.toUser(passwordEncoder));
            } catch (Exception e) {
                log.info("cannot save");
            }
        } else {
            model.addAttribute("error_message", "Пользователь с таким логином уже существует");
            return "registration";
        }

        log.info("Registered new user" + registrationForm.toUser(passwordEncoder).toString());
        return "redirect:/login";

    }
}
