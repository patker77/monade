package com.josephnzi.monade.controller;

import com.josephnzi.monade.dto.RegisterRequest;
import com.josephnzi.monade.exception.EmailException;
import com.josephnzi.monade.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) throws EmailException {
        authService.signup(registerRequest);
        return new ResponseEntity<>("User has been successful register", HttpStatus.OK);

    }

}
