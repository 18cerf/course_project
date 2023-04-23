package com.project.course_project.data.form;

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

import java.util.HashSet;
import java.util.TreeSet;


/*
 *
 * Класс для валидации формы регистрации
 *
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationForm {

    /*
     *
     * поле username
     *
     */
    @NonNull
    @Size(min = 5, message = "Юзернейм должен содержать больше 5 символов")
    private String username;


    /*
     *
     * поле password
     *
     */
    @NonNull
    @Size(min = 6, message = "Пароль должен содержать больше 6 символов")
    private String password;


    /*
     *
     * поле lastname
     *
     */
    @NonNull
    @Pattern(regexp = "^[А-ЯA-Z][a-zа-яё]+$", message = "Некорректно, пример: Иванов")
    private String lastname;


    /*
     *
     * поле name
     *
     */
    @NonNull
    @Pattern(regexp = "^[А-ЯA-Z][a-zа-яё]+$", message = "Некорректно, пример: Иван")
    private String name;


    /*
     *
     * поле phoneNumber
     *
     */
    @NonNull
    @Pattern(regexp = "(^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$)|(^\\s*$)", message = "Укажите, пожалуйста, корректный номер")
    private String phoneNumber;


    /*
     *
     * поле email
     *
     */
    @NonNull
    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message = "Укажите, пожалуйста, корректный email")
    private String email;


    /*
     *
     * Возвращает нового пользователя с паролем зашифрованном bcrypt
     *
     */
    public User toUser(PasswordEncoder passwordEncoder) {
        return new User(username, passwordEncoder.encode(password), lastname, name, phoneNumber, email, new TreeSet<User>());
    }
}
