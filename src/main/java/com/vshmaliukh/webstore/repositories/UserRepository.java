package com.vshmaliukh.webstore.repositories;

import com.vshmaliukh.webstore.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsernameIgnoreCase(String username);

    Page<User> findByUsernameIgnoreCase(String keyword, Pageable pageable);

    Optional<User> readByUsernameIgnoreCase(String username);

}
