package com.project.course_project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
 * Класс веб конфигурации
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /*
     * Добавляем ViewController с путем "/login" и его представление "login"
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }

}
