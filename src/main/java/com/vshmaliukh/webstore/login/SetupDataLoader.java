package com.vshmaliukh.webstore.login;

import com.vshmaliukh.webstore.model.Privilege;
import com.vshmaliukh.webstore.model.Role;
import com.vshmaliukh.webstore.model.User;
import com.vshmaliukh.webstore.repositories.PrivilegeRepository;
import com.vshmaliukh.webstore.repositories.UserRepository;
import com.vshmaliukh.webstore.services.RoleService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup;

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final PrivilegeRepository privilegeRepository;

    public SetupDataLoader(UserRepository userRepository,
                           RoleService roleService,
                           PasswordEncoder passwordEncoder,
                           PrivilegeRepository privilegeRepository) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.privilegeRepository = privilegeRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) return;
        Privilege privilegeRead = createPrivilegeIfNotFound("PRIVILEGE_READ");
        Privilege privilegeUpdate = createPrivilegeIfNotFound("PRIVILEGE_UPDATE");
        Privilege privilegeChangePassword = createPrivilegeIfNotFound("PRIVILEGE_CHANGE_PASSWORD");
        Privilege privilegeReadPrivileges = createPrivilegeIfNotFound("PRIVILEGE_READ_PRIVILEGES");
        Privilege privilegeReadRoles = createPrivilegeIfNotFound("PRIVILEGE_READ_ROLES");

        List<Privilege> devPrivilegesList = Arrays.asList(privilegeRead, privilegeUpdate, privilegeChangePassword,
                privilegeReadPrivileges, privilegeReadRoles);
        List<Privilege> adminPrivilegeList = Arrays.asList(privilegeRead, privilegeUpdate, privilegeChangePassword);


        roleService.createRoleIfNotFound("ROLE_DEV", devPrivilegesList);
        roleService.createRoleIfNotFound("ROLE_ADMIN", adminPrivilegeList);
        // TODO config 'ROLE_STAFF' privileges
        //createRoleIfNotFound("ROLE_STAFF", Collections.singletonList(privilegeRead));
        roleService.createRoleIfNotFound("ROLE_USER", Collections.singletonList(privilegeRead));

        saveDefaultUser("dev", "ROLE_DEV");
        saveDefaultUser("admin", "ROLE_ADMIN");
        saveDefaultUser("user", "ROLE_USER");

        alreadySetup = true;
    }

    private void saveDefaultUser(String devUsername, String userRole) {
        User userByUsername = userRepository.findByUsernameIgnoreCase(devUsername);
        if (userByUsername == null) {
            Role devRole = roleService.findRoleByNameIgnoreCase(userRole);
            User user = new User();
            user.setUsername(devUsername);
            user.setLogInProvider(LogInProvider.LOCAL);
            user.setPassword(passwordEncoder.encode("000"));
            user.setEmail(devUsername + "@mail.com");
            List<Role> roleList = Collections.singletonList(devRole);
            user.setRoles(roleList);
            user.setEnabled(true);
            userRepository.save(user);
        }

    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege();
            privilege.setName(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

}