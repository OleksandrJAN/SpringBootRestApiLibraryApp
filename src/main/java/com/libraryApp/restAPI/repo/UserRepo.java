package com.libraryApp.restAPI.repo;


import com.libraryApp.restAPI.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
