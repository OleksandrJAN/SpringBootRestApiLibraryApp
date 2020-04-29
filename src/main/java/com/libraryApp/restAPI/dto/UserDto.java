package com.libraryApp.restAPI.dto;

import com.libraryApp.restAPI.domain.Review;
import com.libraryApp.restAPI.domain.Role;
import com.libraryApp.restAPI.domain.User;

import java.util.List;
import java.util.Set;

public class UserDto {

    private Long id;
    private String username;
    private Set<Role> roles;
    private List<Review> reviews;

    public UserDto() {
    }


    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.roles = user.getRoles();
        this.reviews = user.getReviews();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
