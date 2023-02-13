package com.vshmaliukh.webstore.dto;

import com.vshmaliukh.webstore.login.LogInProvider;
import com.vshmaliukh.webstore.login.UserRole;
import com.vshmaliukh.webstore.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

// TODO change validation messages

    private Long id;

    @Size(min = 3, max = 50, message = "Please provide a valid 'username' (min = 3, max = 50)")
    private String username;

    @Email(message = "Please provide a valid 'email'")
    private String email;

    @NotEmpty(message = "'logInProvider' cannot be empty")
    private LogInProvider logInProvider;

    @NotEmpty(message = "'role' cannot be empty")
    private UserRole role;

    private boolean enabled;

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.logInProvider = user.getLogInProvider();
        this.role = user.getRole();
    }

}
