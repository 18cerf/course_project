package com.project.course_project.config;

import com.project.course_project.entity.user.User;
import com.project.course_project.repository.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


/*
 * Класс предназначен для работы со Spring Security
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    /*
     * Бин, возвращающий BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * Бин, предназначенный для фильтрации несуществующих пользователей,
     * выбрасывает UsernameNotFoundException, если такого User не найдено.
     */
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            User user = userRepository.findUserByUsername(username);
            if (user != null) return user;
            throw new UsernameNotFoundException("User ‘" + username + "’ not found");
        };
    }

    /*
     * Бин, предназначенный для авторизации запросов.
     * В приложении для незарегистрированных пользователей разрешены только пути:
     * "/login", "/register", "static/styles/**", последний предназначен для разрешения запросов к css стилям.
     * Так же после успешной авторизации на странице login происходит редирект на основную страницу пользователя.
     *
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests()
                .requestMatchers("/users", "/users/**", "static/styles/**")
                .hasRole("USER")
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/", "/**", "/login", "/register", "static/styles/**")
                .permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/users/main")
//                .and()
//                .logout().logoutSuccessUrl("/login")
                .and()
                .build();
    }

}
