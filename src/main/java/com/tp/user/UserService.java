package com.tp.user;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User join(String id, String name) {
        User user = new User();
        user.setId(id);
        user.setName(name);

        return userRepository.save(user);
    }
}