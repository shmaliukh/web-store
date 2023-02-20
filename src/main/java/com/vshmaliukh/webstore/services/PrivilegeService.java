package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.Privilege;
import com.vshmaliukh.webstore.repositories.PrivilegeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
public class PrivilegeService implements EntityValidator<Privilege> {

    private final PrivilegeRepository privilegeRepository;

    public PrivilegeService(PrivilegeRepository privilegeRepository) {
        this.privilegeRepository = privilegeRepository;
    }

    @Transactional
    public Privilege createPrivilegeIfNotFound(String name) {
        Privilege privilege = findPrivilegeByNameIgnoreCase(name);
        if (privilege == null) {
            privilege = new Privilege();
            privilege.setName(name);
            privilegeRepository.save(privilege);
            log.info("created new privilege: '{}'", privilege);
        }
        return privilege;
    }

    public Privilege findPrivilegeByNameIgnoreCase(String name) {
        if (StringUtils.isNotBlank(name)) {
            return privilegeRepository.findByNameIgnoreCase(name);
        }
        log.warn("problem to find privilege by name // name is blank ");
        return null;
    }

    public List<Privilege> readAllRoleList() {
        return privilegeRepository.findAll();
    }

}
