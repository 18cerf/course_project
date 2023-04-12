package com.project.course_project.entity.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)

@ToString
@Entity
@Table(name = "users")
public class User implements UserDetails {

    public User(@NonNull String username, @NonNull String password, @NonNull String lastname, @NonNull String name, @NonNull String phoneNumber, @NonNull String email) {
        this.username = username;
        this.password = password;
        this.lastname = lastname;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.friendList = new ArrayList<>();
    }

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Size(min = 5)
    private String username;

    @NonNull
    private String password;

    @NonNull
    @Pattern(regexp = "^[А-ЯA-Z][a-zа-яё]+$")
    private String lastname;

    @NonNull
    @Pattern(regexp = "^[А-ЯA-Z][a-zа-яё]+$")
    private String name;

    @NonNull
    @Pattern(regexp = "(^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$)|(^\\s*$)", message = "Укажите, пожалуйста, корректный номер")
    private String phoneNumber;

    @NonNull
    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message = "Укажите, пожалуйста, корректный email")
    private String email;


    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<User> friendList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"), new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

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
}
