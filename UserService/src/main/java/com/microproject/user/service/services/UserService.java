package com.microproject.user.service.services;

import com.microproject.user.service.entities.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);

    //get all user
    List<User> getAllUser();

    //get single user of given id

    User getUserById(String userId);



}
