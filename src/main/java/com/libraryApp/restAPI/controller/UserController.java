package com.libraryApp.restAPI.controller;

import com.libraryApp.restAPI.domain.Role;
import com.libraryApp.restAPI.domain.User;
import com.libraryApp.restAPI.dto.PasswordDto;
import com.libraryApp.restAPI.dto.UserDto;
import com.libraryApp.restAPI.security.jwt.JwtUser;
import com.libraryApp.restAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getUsers() {
        List<UserDto> users = userService.getUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/users/{user:[\\d]+}")
    public ResponseEntity<UserDto> getUser(@PathVariable("user") User userProfile) {
        if (userProfile != null) {
            UserDto userDto = new UserDto(userProfile);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/users/{username:[\\D]+}")
    public ResponseEntity<UserDto> getUser(@PathVariable("username") String username) {
        if (!StringUtils.isEmpty(username)) {
            User user = userService.getByUsername(username);
            if (user != null) {
                return new ResponseEntity<>(new UserDto(user), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @DeleteMapping("/users/{user:[\\d]+}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity deleteWriter(@PathVariable("user") User userProfile) {
        if (userProfile != null) {
            userService.deleteUser(userProfile);
            return new ResponseEntity(HttpStatus.OK);
        }

        return ResponseEntity.badRequest().body("User not found");
    }


    @PutMapping("/users/{user:[\\d]+}/settings/password")
    public ResponseEntity updatePassword(
            @Valid @RequestBody PasswordDto passwordDto,
            @PathVariable("user") User userProfile,
            @AuthenticationPrincipal JwtUser currentUser
    ) {
        if (userProfile.getId().equals(currentUser.getId())) {
            String currentPasswordConfirmation = passwordDto.getCurrentPasswordConfirmation();
            boolean isOldPassCorrect = userService.isCorrectUserPassword(currentPasswordConfirmation, userProfile);
            boolean isNewPassCorrect = passwordDto.isCorrectNewPasswords();

            if (isOldPassCorrect && isNewPassCorrect) {
                userService.updateUserPassword(passwordDto.getNewPassword(), userProfile);
                return new ResponseEntity(HttpStatus.OK);
            }
            return ResponseEntity.badRequest().body("Incorrect password data");
        }
        return ResponseEntity.badRequest().body("Incorrect user data");
    }

    @PutMapping("/users/{user:[\\d]+}/settings/role")
    public ResponseEntity updateRole(
            @RequestBody Set<Role> userRoles,
            @PathVariable("user") User userProfile,
            @AuthenticationPrincipal JwtUser currentUser
    ) {
        if (!userRoles.isEmpty()) {
            boolean currentUserNotOnTheirProfile = !userProfile.getId().equals(currentUser.getId());
            boolean isCurrentUSerAdmin = currentUser.isAdmin();
            if (currentUserNotOnTheirProfile && isCurrentUSerAdmin) {
                userService.updateUserRole(userRoles, userProfile);
                return ResponseEntity.ok(userRoles);
            }
            return ResponseEntity.badRequest().body("Not enough rights");
        }
        return ResponseEntity.badRequest().body("Incorrect roles");
    }
}
