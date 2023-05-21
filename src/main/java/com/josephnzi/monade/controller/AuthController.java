package com.josephnzi.monade.controller;

import com.josephnzi.monade.dto.AuthResponse;
import com.josephnzi.monade.dto.LoginRequest;
import com.josephnzi.monade.dto.RegisterRequest;
import com.josephnzi.monade.exception.MonadeExceptions;
import com.josephnzi.monade.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody RegisterRequest registerRequest) throws MonadeExceptions {

        return ResponseEntity.ok(authService.signup(registerRequest));

    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) throws MonadeExceptions {
      return ResponseEntity.ok(authService.login(loginRequest));
    }
    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String>accountVerification(@PathVariable String token) throws MonadeExceptions {
        authService.verifyEmail(token);
        return new ResponseEntity<>("your account has been successful verified!",HttpStatus.OK);
    }


}
