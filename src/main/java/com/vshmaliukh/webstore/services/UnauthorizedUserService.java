package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.UnauthorizedUser;
import com.vshmaliukh.webstore.repositories.UnauthorizedUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UnauthorizedUserService {

    final UnauthorizedUserRepository unauthorizedUserRepository;

    boolean existsById(Long id){
        return unauthorizedUserRepository.existsById(id);
    }

}
