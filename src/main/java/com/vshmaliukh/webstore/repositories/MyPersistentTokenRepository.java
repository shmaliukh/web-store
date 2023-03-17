package com.vshmaliukh.webstore.repositories;

import com.vshmaliukh.webstore.model.PersistentToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyPersistentTokenRepository extends JpaRepository<PersistentToken, String> {

    // repository to store 'rememberMe' services tokens in a database

    PersistentToken findBySeries(String series);

    List<PersistentToken> findAllByUsernameIgnoreCase(String username);

}
