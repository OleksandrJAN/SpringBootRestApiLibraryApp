package com.libraryApp.restAPI.service;


import com.libraryApp.restAPI.domain.Role;
import com.libraryApp.restAPI.domain.User;
import com.libraryApp.restAPI.dto.UserDto;
import com.libraryApp.restAPI.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }


    public boolean isUserExists(String username) {
        User userFromDb = userRepo.findByUsername(username);
        return userFromDb != null;
    }

    public boolean createUser(String username, String password) {
        if (isUserExists(username)) {
            return false;
        }

        User user = new User();
        user.setUsername(username);
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(password));

        userRepo.save(user);
        return true;
    }

    public List<UserDto> getUsers() {
        List<User> users = userRepo.findAll();

        return users.stream().map(
                user -> new UserDto(user)
        ).collect(Collectors.toList());
    }

    public User getByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public void deleteUser(User user) {
        userRepo.delete(user);
    }

}
