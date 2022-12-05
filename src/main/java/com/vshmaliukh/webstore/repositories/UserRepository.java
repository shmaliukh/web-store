package com.vshmaliukh.webstore.repositories;

import com.vshmaliukh.webstore.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User getUserEntityByUsername(@Param("username") String username);

}
