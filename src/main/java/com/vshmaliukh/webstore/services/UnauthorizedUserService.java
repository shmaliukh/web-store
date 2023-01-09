package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.UnauthorizedUser;
import com.vshmaliukh.webstore.repositories.UnauthorizedUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
        Timestamp timestamp = new Timestamp(date.getTime());
        List<UnauthorizedUser> users = unauthorizedUserRepository.findAll();
        List<UnauthorizedUser> usersToRemove = users.stream().filter(o->new Timestamp(o.getCreatedAt().getTime()).getTime()<(timestamp.getTime()-24*60*60)).collect(Collectors.toList());
        unauthorizedUserRepository.deleteAll(usersToRemove);
    }

    public UnauthorizedUser createUnauthorizedUser(){
        UnauthorizedUser unauthorizedUser = new UnauthorizedUser();
        unauthorizedUserRepository.save(unauthorizedUser);
        return unauthorizedUser;
    }

}
