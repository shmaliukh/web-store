package com.vshmaliukh.webstore.model;

import com.vshmaliukh.webstore.login.LogInProvider;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

import static com.vshmaliukh.webstore.ConstantsForEntities.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = USER_TABLE,
        uniqueConstraints = {@UniqueConstraint(columnNames = {USER_NAME_COLUMN})})
public class User extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = USER_NAME_COLUMN, nullable = false)
    private String username;

    @Column(name = USER_EMAIL_COLUMN)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = USER_LOG_IN_PROVIDER)
    private LogInProvider logInProvider;

    private String role;

    @ToString.Exclude
    private String password;

    private boolean enabled;

}
