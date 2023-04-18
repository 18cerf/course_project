package com.project.course_project.service.image;

import com.project.course_project.entity.image.UserImage;
import com.project.course_project.entity.user.User;
import com.project.course_project.repository.image.UserImageRepository;
import com.project.course_project.repository.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserImageService {
    private final UserImageRepository userImageRepository;
    private final UserRepository userRepository;

    public UserImageService(UserImageRepository userImageRepository, UserRepository userRepository) {
        this.userImageRepository = userImageRepository;
        this.userRepository = userRepository;
    }

    public void saveUserImage(byte[] imageData, User user) {
        UserImage userImage = new UserImage();
        userImage.setUser(user);
        userImage.setImageData(imageData);
        userImageRepository.save(userImage);
    }

    public byte[] getUserImage(Long userId) {
        Optional<UserImage> userImageOptional = userImageRepository.findByUserId(userId);
        if (userImageOptional.isPresent()) {
            return userImageOptional.get().getImageData();
        } else {
            return null;
        }
    }
}
