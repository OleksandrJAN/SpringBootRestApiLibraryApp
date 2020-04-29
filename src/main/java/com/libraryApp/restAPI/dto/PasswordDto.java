package com.libraryApp.restAPI.dto;

import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PasswordDto {

    @NotBlank
    @Size(min = 3, max = 255)
    private String currentPasswordConfirmation;

    @NotBlank
    @Size(min = 3, max = 255)
    private String newPassword;

    @NotBlank
    @Size(min = 3, max = 255)
    private String newPasswordConfirmation;


    public Boolean isCorrectNewPasswords() {
        return  !StringUtils.isEmpty(newPassword) &&
                !StringUtils.isEmpty(newPasswordConfirmation) &&
                newPassword.equals(newPasswordConfirmation);
    }

    public String getCurrentPasswordConfirmation() {
        return currentPasswordConfirmation;
    }

    public void setCurrentPasswordConfirmation(String currentPasswordConfirmation) {
        this.currentPasswordConfirmation = currentPasswordConfirmation;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordConfirmation() {
        return newPasswordConfirmation;
    }

    public void setNewPasswordConfirmation(String newPasswordConfirmation) {
        this.newPasswordConfirmation = newPasswordConfirmation;
    }
}
