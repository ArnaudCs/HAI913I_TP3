package com.example.demo.service;

import com.example.demo.api.model.User;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private List<User> userList;

    public UserService(){
        userList = new ArrayList<>();

        User user = new User(1, "Adam", 22, "adam.s@yahoo.fr", "boubli");
        User user2 = new User(2, "Arnaud", 22, "arnaud.c@yahoo.fr", "boubla");
        userList.addAll(Arrays.asList(user, user2));
    }
    public Optional<User> getUser(Integer id) {
        Optional optional = Optional.empty();
        for(User user: userList){
            if(id == user.getId()){
                optional = Optional.of(user);
                return optional;
            }
        }
        return optional;
    };
}
