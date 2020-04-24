package com.libraryApp.restAPI.dto;

import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SignUpForm {

    @NotBlank
    @Size(min = 3, max = 255)
    private String username;

    @NotBlank
    @Size(min = 3, max = 255)
    private String password;

    @NotBlank
    @Size(min = 3, max = 40)
    private String passwordConfirmation;


    public Boolean isCorrectPasswords() {
        return  !StringUtils.isEmpty(password) &&
                !StringUtils.isEmpty(passwordConfirmation) &&
                password.equals(passwordConfirmation);
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

}
