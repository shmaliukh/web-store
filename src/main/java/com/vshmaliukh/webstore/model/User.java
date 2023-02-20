package com.vshmaliukh.webstore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vshmaliukh.webstore.login.LogInProvider;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users")
public class User extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Min(value = 1 , message = "Please provide a valid 'user_id' (min = 1)")
    @Column(name = "user_id")
    private Long id;

    //    @Size(min = 3, max = 50, message = "Please provide a valid 'username' (min = 3, max = 50)")
    @Column(unique = true)
    private String username;

    //    @Email(message = "Please provide a valid 'email'")
    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
//    @NotEmpty(message = "'role' cannot be empty")
    @Column(name = "log_in_provider", nullable = false)
    private LogInProvider logInProvider;

    @ManyToMany
    @JoinTable(
            name = "Users_Roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "role_id"))
    @ToString.Exclude
    @JsonIgnore
    private Collection<Role> roles;

    @ToString.Exclude
//    @Size(min = 4, max = 50, message = "Please provide a valid 'password' (min = 4, max = 50)")
    private String password;

    private boolean enabled;

}
