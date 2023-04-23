package com.project.course_project.service.image;

import com.project.course_project.entity.image.UserImage;
import com.project.course_project.entity.user.User;
import com.project.course_project.repository.image.UserImageRepository;
import com.project.course_project.repository.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;


/*
 * Сервис-класс для работы с UserImageRepository
 */
@Service
public class UserImageService {
    private final UserImageRepository userImageRepository;

    public UserImageService(UserImageRepository userImageRepository) {
        this.userImageRepository = userImageRepository;
    }

    /*
     * Метод сохраняет картинку пользователя в базу
     */
    public void saveUserImage(byte[] imageData, User user) {
        UserImage userImage = new UserImage();
        userImage.setUser(user);
        userImage.setImageData(imageData);
        userImageRepository.save(userImage);
    }

    /*
     * Метод возвращает картинку по ID пользователя
     */
    public byte[] getUserImage(Long userId) {
        Optional<UserImage> userImageOptional = userImageRepository.findFirstByUserId(userId);
        if (userImageOptional.isPresent()) {
            return userImageOptional.get().getImageData();
        } else {
            return null;
        }
    }
}
