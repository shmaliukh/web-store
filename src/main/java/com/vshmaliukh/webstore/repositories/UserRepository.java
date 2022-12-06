package com.vshmaliukh.webstore.repositories;

import com.vshmaliukh.webstore.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    UserEntity getUserEntityByUsername(@Param("username") String username);

}
