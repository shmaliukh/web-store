package com.vshmaliukh.webstore.controllers.rest;

import com.vshmaliukh.webstore.model.Privilege;
import com.vshmaliukh.webstore.model.Role;
import com.vshmaliukh.webstore.repositories.PrivilegeRepository;
import com.vshmaliukh.webstore.services.RoleService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoleAndPrivilegeController {

    private final RoleService roleService;
    private final PrivilegeRepository privilegeRepository;

    public RoleAndPrivilegeController(RoleService roleService, PrivilegeRepository privilegeRepository) {
        this.roleService = roleService;
        this.privilegeRepository = privilegeRepository;
    }

    @GetMapping("/privileges")
    @PreAuthorize("hasAuthority('PRIVILEGE_READ_PRIVILEGES')")
    public ResponseEntity<List<Privilege>> readAllPrivilegeList(){
        List<Privilege> privilegeList = privilegeRepository.findAll();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(privilegeList);
    }

    @GetMapping("/roles")
    @PreAuthorize("hasAuthority('PRIVILEGE_READ_ROLES')")
    public ResponseEntity<List<Role>> readAllRoleList(){
        List<Role> roleList = roleService.readAllRoleList();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(roleList);
    }

}
