package com.project.course_project.forms;

import com.project.course_project.entity.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationForm {

    @NonNull
    @Size(min = 5)
    private String username;

    @NonNull
    private String password;

    @NonNull
    @Pattern(regexp = "^[А-ЯA-Z][a-zа-яё]+$", message = "Ошибка, никнейм должен содержать больше 5 символов")
    private String lastname;

    @NonNull
    @Pattern(regexp = "^[А-ЯA-Z][a-zа-яё]+$", message = "Ошибка, пароль должен содержать больше 6 символов")
    private String name;

    @NonNull
    @Pattern(regexp = "(^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$)|(^\\s*$)", message = "Укажите, пожалуйста, корректный номер")
    private String phoneNumber;

    @NonNull
    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message = "Укажите, пожалуйста, корректный email")
    private String email;


    public User toUser(PasswordEncoder passwordEncoder) {
        return new User(username, passwordEncoder.encode(password), lastname, name, phoneNumber, email);
    }
}
