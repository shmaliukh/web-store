package com.vshmaliukh.webstore.repositories;

import com.vshmaliukh.webstore.model.UnauthorizedUser;
import com.vshmaliukh.webstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnauthorizedUserRepository extends JpaRepository<UnauthorizedUser, Long> {

    boolean existsById(Long id);


}
