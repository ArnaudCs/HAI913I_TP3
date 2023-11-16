package com.example.demo.api.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.api.model.User;


public interface UserRepository extends MongoRepository<User, String> {

}
