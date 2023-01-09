package com.microproject.user.service.serviceimpl;

import com.microproject.user.service.entities.User;
import com.microproject.user.service.exception.ResourceNotFoundException;
import com.microproject.user.service.repositories.UserRepository;
import com.microproject.user.service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public User saveUser(User user) {
        //it will generate unique id
       String randomUserId= UUID.randomUUID().toString();
       user.setUserId(randomUserId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(String userId) {
        //ager id mila to database me se dega aur ager nhi mila to we need to handle this globally
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Resource not found with given id "+userId));

    }
}
