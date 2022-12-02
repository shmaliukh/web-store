package com.vshmaliukh.webstore.entities;

import com.vshmaliukh.webstore.LogInProvider;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

import static com.vshmaliukh.webstore.ConstantsForEntities.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = USER_TABLE,
        uniqueConstraints = {@UniqueConstraint(columnNames = {USER_NAME_COLUMN})})
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID_COLUMN, nullable = false)
    private Long id;

    @Column(name = USER_NAME_COLUMN, nullable = false)
    private String username;

    @Column(name = USER_EMAIL_COLUMN, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = USER_LOG_IN_PROVIDER)
    private LogInProvider logInProvider;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = USER_ROLE_TABLE,
            joinColumns = @JoinColumn(name = USER_ID_COLUMN),
            inverseJoinColumns = @JoinColumn(name = ROLE_ID_COLUMN))
    private Set<RoleEntity> roles = new HashSet<>();


}
