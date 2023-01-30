package com.vshmaliukh.webstore.repositories;

import com.vshmaliukh.webstore.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User getUserById(Long id);

    User getUserByUsername(String username);

    Page<User> findByUsernameIgnoreCase(String keyword, Pageable pageable);

}
