package com.vshmaliukh.webstore.login;

import com.vshmaliukh.webstore.model.Privilege;
import com.vshmaliukh.webstore.model.Role;
import com.vshmaliukh.webstore.model.User;
import com.vshmaliukh.webstore.repositories.PrivilegeRepository;
import com.vshmaliukh.webstore.repositories.RoleRepository;
import com.vshmaliukh.webstore.repositories.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PrivilegeRepository privilegeRepository;

    public SetupDataLoader(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           PrivilegeRepository privilegeRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.privilegeRepository = privilegeRepository;
    }


    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) return;
        Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege createPrivilege = createPrivilegeIfNotFound("CREATE_PRIVILEGE");
        Privilege updatePrivilege = createPrivilegeIfNotFound("UPDATE_PRIVILEGE");
        Privilege deletePrivilege = createPrivilegeIfNotFound("DELETE_PRIVILEGE");

        List<Privilege> devPrivilegesList = Arrays.asList(readPrivilege, updatePrivilege);
        List<Privilege> adminPrivilegeList = Arrays.asList(readPrivilege, updatePrivilege);

        createRoleIfNotFound("ROLE_DEV", devPrivilegesList);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivilegeList);
        // TODO config 'ROLE_STAFF' privileges
        createRoleIfNotFound("ROLE_STAFF", Collections.singletonList(readPrivilege));
        createRoleIfNotFound("ROLE_USER", Collections.singletonList(readPrivilege));

        saveDefaultDev();
        saveDefaultAdmin();
        saveDefaultUser();

        alreadySetup = true;
    }

    private void saveDefaultDev() {
        Role devRole = roleRepository.findByName("ROLE_DEV");
        User user = new User();
        user.setUsername("dev");
        user.setLogInProvider(LogInProvider.LOCAL);
        user.setPassword(passwordEncoder.encode("000"));
        user.setEmail("dev@mail.com");
        user.setRoles(Collections.singletonList(devRole));
        user.setEnabled(true);
        userRepository.save(user);
    }

    private void saveDefaultAdmin() {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        User user = new User();
        user.setUsername("admin");
        user.setLogInProvider(LogInProvider.LOCAL);
        user.setPassword(passwordEncoder.encode("000"));
        user.setEmail("admin@mail.com");
        user.setRoles(Collections.singletonList(adminRole));
        user.setEnabled(true);
        userRepository.save(user);
    }

    private void saveDefaultUser() {
        Role adminRole = roleRepository.findByName("ROLE_USER");
        User user = new User();
        user.setUsername("user");
        user.setLogInProvider(LogInProvider.LOCAL);
        user.setPassword(passwordEncoder.encode("000"));
        user.setEmail("user@mail.com");
        user.setRoles(Collections.singletonList(adminRole));
        user.setEnabled(true);
        userRepository.save(user);
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

    @Transactional
    void createRoleIfNotFound(String name, Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role();
            role.setName(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
    }

}