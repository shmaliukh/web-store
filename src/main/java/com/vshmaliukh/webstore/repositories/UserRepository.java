package com.vshmaliukh.webstore.repositories;

import com.vshmaliukh.webstore.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import static com.vshmaliukh.webstore.ConstantsForEntities.*;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User getUserById(@Param(USER_ID_COLUMN) Long id);

    User getUserByUsername(@Param(USER_NAME_COLUMN) String username);

}
