package com.josephnzi.monade.controller;

import com.josephnzi.monade.dto.RegisterRequest;
import com.josephnzi.monade.exception.MonadeExceptions;
import com.josephnzi.monade.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) throws MonadeExceptions {
        authService.signup(registerRequest);
        return new ResponseEntity<>("User has been successful register", HttpStatus.OK);

    }
    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String>accountVerification(@PathVariable String token) throws MonadeExceptions {
        authService.verifyEmail(token);
        return new ResponseEntity<>("your account has been successful verified!",HttpStatus.OK);
    }

}
