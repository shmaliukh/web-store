package com.vshmaliukh.webstore.repositories;

import com.vshmaliukh.webstore.model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    Privilege findByNameIgnoreCase(String name);

}
