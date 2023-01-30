package com.vshmaliukh.webstore.model;

import com.vshmaliukh.webstore.login.LogInProvider;
import lombok.*;

import javax.persistence.*;

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
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "log_in_provider")
    private LogInProvider logInProvider;

    private String role;

    @ToString.Exclude
    private String password;

    private boolean enabled;

}
