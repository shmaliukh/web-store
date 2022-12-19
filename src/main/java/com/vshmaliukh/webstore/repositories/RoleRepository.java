package com.vshmaliukh.webstore.repositories;

import com.vshmaliukh.webstore.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    
}
