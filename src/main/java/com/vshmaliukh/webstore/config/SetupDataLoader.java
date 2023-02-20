package com.vshmaliukh.webstore.config;

import com.vshmaliukh.webstore.model.Privilege;
import com.vshmaliukh.webstore.model.Role;
import com.vshmaliukh.webstore.model.User;
import com.vshmaliukh.webstore.services.PrivilegeService;
import com.vshmaliukh.webstore.services.RoleService;
import com.vshmaliukh.webstore.services.UserService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    public static final String DEFAULT_PASSWORD = "000";

    private boolean alreadySetup;

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final PrivilegeService privilegeService;


    public SetupDataLoader(UserService userService,
                           RoleService roleService,
                           PasswordEncoder passwordEncoder,
                           PrivilegeService privilegeService) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.privilegeService = privilegeService;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) return;
        Privilege privilegeRead = privilegeService.createPrivilegeIfNotFound("PRIVILEGE_READ");
        Privilege privilegeUpdate = privilegeService.createPrivilegeIfNotFound("PRIVILEGE_UPDATE");
        Privilege privilegeChangePassword = privilegeService.createPrivilegeIfNotFound("PRIVILEGE_CHANGE_PASSWORD");
        Privilege privilegeReadPrivileges = privilegeService.createPrivilegeIfNotFound("PRIVILEGE_READ_PRIVILEGES");
        Privilege privilegeReadRoles = privilegeService.createPrivilegeIfNotFound("PRIVILEGE_READ_ROLES");

        List<Privilege> devPrivilegesList = Arrays.asList(privilegeRead, privilegeUpdate, privilegeChangePassword,
                privilegeReadPrivileges, privilegeReadRoles);
        List<Privilege> adminPrivilegeList = Arrays.asList(privilegeRead, privilegeUpdate, privilegeChangePassword);


        roleService.createRoleIfNotFound("ROLE_DEV", devPrivilegesList);
        roleService.createRoleIfNotFound("ROLE_ADMIN", adminPrivilegeList);
        // TODO config 'ROLE_STAFF' privileges
        //createRoleIfNotFound("ROLE_STAFF", Collections.singletonList(privilegeRead));
        roleService.createRoleIfNotFound("ROLE_USER", Collections.singletonList(privilegeRead));

        createDefaultUserIfNotFound("dev", "ROLE_DEV");
        createDefaultUserIfNotFound("admin", "ROLE_ADMIN");
        createDefaultUserIfNotFound("user", "ROLE_USER");

        alreadySetup = true;
    }

    private void createDefaultUserIfNotFound(String username, String roleName) {
        User user;
        Optional<User> optionalUser = userService.readUserByUsernameIgnoreCase(username);
        Role role = roleService.findRoleByNameIgnoreCase(roleName);
        List<Role> roleList = Collections.singletonList(role);
        if(!optionalUser.isPresent()) {
            user = userService.createBaseUser(username, username + "@mail.com", passwordEncoder.encode(DEFAULT_PASSWORD), true);
            user.setRoles(roleList);
            userService.save(user);
        }
    }

}