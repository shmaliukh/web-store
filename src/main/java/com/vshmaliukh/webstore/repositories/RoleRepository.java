package com.vshmaliukh.webstore.repositories;

import com.vshmaliukh.webstore.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByNameIgnoreCase(String name);

}
