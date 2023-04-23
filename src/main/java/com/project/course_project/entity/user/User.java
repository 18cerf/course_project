package com.project.course_project.entity.user;

import com.project.course_project.entity.date.DateTime;
import com.project.course_project.entity.image.UserImage;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;



/*
 * Entity-класс, предназначенный для хранения данных пользователей
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Entity
@Table(name = "users")
public class User implements UserDetails {

    /*
     * Конструктор с необходимыми для инициализации полями
     */
    public User(@NonNull String username, @NonNull String password, @NonNull String lastname, @NonNull String name, @NonNull String phoneNumber, @NonNull String email, Set friends) {
        this.username = username;
        this.password = password;
        this.lastname = lastname;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.friends = friends;
    }

    /*
     * Id сущности
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /*
     * username сущности
     */
    @NonNull
    @Size(min = 5, message = "Юзернейм должен содержать больше 5 символов")
    private String username;


    /*
     * password сущности
     */
    @NonNull
    @Size(min = 6, message = "Пароль должен содержать больше 6 символов")
    private String password;


    /*
     * lastname сущности
     */
    @NonNull
    @Pattern(regexp = "^[А-ЯA-Z][a-zа-яё]+$", message = "Пример: Иванов")
    private String lastname;


    /*
     * name сущности
     */
    @NonNull
    @Pattern(regexp = "^[А-ЯA-Z][a-zа-яё]+$", message = "Пример: Иван")
    private String name;


    /*
     * phoneNumber сущности
     */
    @NonNull
    @Pattern(regexp = "(^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$)|(^\\s*$)", message = "Укажите, пожалуйста, корректный номер")
    private String phoneNumber;


    /*
     * email сущности
     */
    @NonNull
    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message = "Укажите, пожалуйста, корректный email")
    private String email;


    /*
     * friends сущности
     *
     * Связываем таблицы аннотацией @ManyToMany, джойним к таблице "user_friends"
     */
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<User> friends;


    /*
     * loginTimes сущности
     *
     * Связываем таблицы аннотацией @OneToMany, джойним к колонке "date_time_id"
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "date_time_id")
    private Set<DateTime> loginTimes = new HashSet<>();


    /*
     * image сущности
     *
     * Связываем таблицы аннотацией @OneToOne, джойним к колонке "user_image_id"
     */
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_image_id")
    private UserImage image;


    /*
     *  геттер loginTimes
     *
     */
    public Set<DateTime> getLoginTimes() {
        return loginTimes;
    }

    /*
     * сеттер loginTimes
     */
    public void setLoginTimes(Set<DateTime> loginTimes) {
        this.loginTimes = loginTimes;
    }

    /*
     * сеттер friend
     */
    public void setFriend(User friend) {
        friends.add(friend);
    }

    /*
     * геттер friends
     */
    public Set<User> getFriends() {
        return friends;
    }

    /*
     *
     * return ролей пользователей, требование Spring Security
     *
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"), new SimpleGrantedAuthority("ROLE_ADMIN"));
    }


    /*
     *
     * требование Spring Security
     *
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /*
     *
     * требование Spring Security
     *
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /*
     *
     * требование Spring Security
     *
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    /*
     *
     * требование Spring Security
     *
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (!username.equals(user.username)) return false;
        if (!lastname.equals(user.lastname)) return false;
        if (!phoneNumber.equals(user.phoneNumber)) return false;
        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + username.hashCode();
        result = 31 * result + lastname.hashCode();
        result = 31 * result + phoneNumber.hashCode();
        result = 31 * result + email.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "username = " + username + ", " +
                "password = " + password + ", " +
                "lastname = " + lastname + ", " +
                "name = " + name + ", " +
                "phoneNumber = " + phoneNumber + ", " +
                "email = " + email + ")";
    }
}
