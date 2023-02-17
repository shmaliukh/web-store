package com.vshmaliukh.webstore.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "Roles")
public class Role extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "roles")
    @ToString.Exclude
    private Collection<User> users;

    @ManyToMany
    @JoinTable(
            name = "Roles_Privileges",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "role_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "privilege_id", referencedColumnName = "privilege_id"))
    @ToString.Exclude
    private Collection<Privilege> privileges;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;

        Role role = (Role) o;

        if (getId() != null ? !getId().equals(role.getId()) : role.getId() != null) return false;
        if (getName() != null ? !getName().equals(role.getName()) : role.getName() != null) return false;
        if (getUsers() != null ? !getUsers().equals(role.getUsers()) : role.getUsers() != null) return false;
        return getPrivileges() != null ? getPrivileges().equals(role.getPrivileges()) : role.getPrivileges() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getUsers() != null ? getUsers().hashCode() : 0);
        result = 31 * result + (getPrivileges() != null ? getPrivileges().hashCode() : 0);
        return result;
    }

}
