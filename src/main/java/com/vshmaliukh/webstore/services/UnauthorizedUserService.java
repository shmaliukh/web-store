package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.UnauthorizedUser;
import com.vshmaliukh.webstore.repositories.UnauthorizedUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
@AllArgsConstructor
public class UnauthorizedUserService {

    final UnauthorizedUserRepository unauthorizedUserRepository;

    public boolean existsById(Long id){

        return unauthorizedUserRepository.existsById(id);
    }

    public UnauthorizedUser getUserById(Long id){
        return unauthorizedUserRepository.getReferenceById(id);
    }

    public void removeUnauthorizedUser(Long id){
        unauthorizedUserRepository.delete(getUserById(id));
    }

    public void removeOldUsers(){
        Date date = new Date();
        date.setTime(date.getTime()-24*60*60);
        unauthorizedUserRepository.deleteAll(unauthorizedUserRepository.readUnauthorizedUsersByCreatedAtBefore(date));
    }

    public UnauthorizedUser createUnauthorizedUser(){
        return unauthorizedUserRepository.save(new UnauthorizedUser());
    }

    public void saveUser(UnauthorizedUser unauthorizedUser){
        unauthorizedUserRepository.save(unauthorizedUser);
    }

}
