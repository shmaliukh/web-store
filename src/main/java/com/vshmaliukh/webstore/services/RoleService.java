package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.Privilege;
import com.vshmaliukh.webstore.model.Role;
import com.vshmaliukh.webstore.repositories.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class RoleService implements EntityValidator<Role> {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> readAllRoleList() {
        return roleRepository.findAll();
    }

    @Transactional
    public void createRoleIfNotFound(String name, Collection<Privilege> privileges) {
        Role role = roleRepository.findByNameIgnoreCase(name);
        if (role == null) {
            role = new Role();
            role.setName(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
            log.info("created new role: '{}' with privileges: '{}'", role, privileges);
        }
    }

    public Role findRoleByNameIgnoreCase(String name) {
        if (StringUtils.isNotBlank(name)) {
            return roleRepository.findByNameIgnoreCase(name);
        }
        log.warn("problem to find role // name is blank ");
        return null;
    }

}
