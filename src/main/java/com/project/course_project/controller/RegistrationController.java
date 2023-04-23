package com.project.course_project.controller;

import com.project.course_project.repository.user.UserRepository;
import com.project.course_project.data.form.RegistrationForm;
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


/*
 * Контроллер, предназначенный для регистрации новых пользователей
 */
@Controller
@RequestMapping("/register")
@Slf4j
public class RegistrationController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;


    /*
     * Внедряем бины userRepository и passwordEncoder
     */
    public RegistrationController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    /*
     * ModelAttribute "registerForm" предназначен для заполнения его данными о User
     * + валидации входящей информации о новом User.
     *
     */
    @ModelAttribute(name = "registerForm")
    public RegistrationForm registrationForm() {
        return new RegistrationForm();
    }

    /*
     * Возвращает представление для регистрации.
     */
    @GetMapping
    public String registerForm() {
        return "registration";
    }


    /*
     *
     * В случае успешной валидации данных происходит редирект на страницу входа
     *
     * Если данные валидацию не прошли, возвращается на страницу регистрации.
     *
     */
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
