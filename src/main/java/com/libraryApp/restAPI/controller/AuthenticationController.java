package com.libraryApp.restAPI.controller;

import com.libraryApp.restAPI.domain.User;
import com.libraryApp.restAPI.dto.ResponseMessage;
import com.libraryApp.restAPI.dto.SignInForm;
import com.libraryApp.restAPI.dto.AuthenticationResponseDto;
import com.libraryApp.restAPI.dto.SignUpForm;
import com.libraryApp.restAPI.security.jwt.JwtTokenProvider;
import com.libraryApp.restAPI.security.jwt.JwtUser;
import com.libraryApp.restAPI.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/auth/")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public AuthenticationController(
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider,
            UserService userService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody SignInForm signInRequest) {
        try {
            String username = signInRequest.getUsername();
            User user = userService.getByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("User '" + username + "' not found");
            }

            String password = signInRequest.getPassword();
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            String token = jwtTokenProvider.createToken(user);
            AuthenticationResponseDto response = new AuthenticationResponseDto(
                    token,
                    username,
                    authentication.getAuthorities()
            );

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @PostMapping("signup")
    public ResponseEntity registerUser(@Valid @RequestBody SignUpForm signUpRequest) {
        if (!signUpRequest.isCorrectPasswords()) {
            return new ResponseEntity<>(
                    new ResponseMessage("Fail -> Passwords is incorrect!"),
                    HttpStatus.BAD_REQUEST
            );
        }


        String username = signUpRequest.getUsername();
        String password = signUpRequest.getPassword();
        if (!userService.create(username, password)) {
            return new ResponseEntity<>(
                    new ResponseMessage("Fail -> Username is already taken!"),
                    HttpStatus.BAD_REQUEST
            );
        }

        return new ResponseEntity<>(new ResponseMessage("User registered successfully!"), HttpStatus.OK);
    }
}
