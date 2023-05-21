package com.josephnzi.monade.service;

import com.josephnzi.monade.dto.AuthResponse;
import com.josephnzi.monade.dto.LoginRequest;
import com.josephnzi.monade.dto.RegisterRequest;
import com.josephnzi.monade.exception.AccountVerificationException;
import com.josephnzi.monade.exception.MonadeExceptions;
import com.josephnzi.monade.exception.UsernameNotFoundExcep;
import com.josephnzi.monade.model.NotificationEmail;
import com.josephnzi.monade.model.Role;
import com.josephnzi.monade.model.User;
import com.josephnzi.monade.model.VerificationToken;
import com.josephnzi.monade.repository.UserRepository;
import com.josephnzi.monade.repository.VerificationTokenRepository;
import com.josephnzi.monade.security.JsonWebToken;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor

public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JsonWebToken jsonWebToken;

    @Transactional
    public AuthResponse signup(RegisterRequest registerRequest) throws MonadeExceptions {
        var user =  User.builder()
                .userName(registerRequest.getUsername())
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .created(Instant.now())
                .enabled(false)
                .role(Role.USER)
                        .build();
                userRepository.save(user);
                String token = generateUserToken(user);
                mailService.sendEmail(new NotificationEmail("Please verify your email Account ","Thank you for your registration at Monade. Please Click on the link below to verify your account "+
                        "http://locahost:8080/api/auth/accountVerification/"+token,user.getEmail()));
                var jwtoken = jsonWebToken.generateTokens(user);
                return AuthResponse.builder()
                        .tokens(jwtoken)
                        .username(user.getUsername())
                        .build();

    }

    private String generateUserToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(user);
        verificationToken.setToken(token);
        verificationToken.setExpiryDate(Instant.now());
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyEmail(String token) throws MonadeExceptions {
       Optional<VerificationToken> verificationToken= verificationTokenRepository.findByToken(token);
       verificationToken.orElseThrow(()->new AccountVerificationException("Invalide Token"));
       fetchUserAndEnableAccount(verificationToken.get());
    }


    @Transactional
    public void fetchUserAndEnableAccount(VerificationToken verificationToken) throws MonadeExceptions {
        String username = verificationToken.getUser().getUsername();
        User user =userRepository.findByUserName(username).orElseThrow(() -> new UsernameNotFoundExcep("User with the username: "+username +" not found"));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest loginRequest) throws UsernameNotFoundExcep {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));
        User user = userRepository.findByUserName(loginRequest.getUsername()).orElseThrow(
                () -> new UsernameNotFoundExcep("User with the username: "+loginRequest.getUsername() +" not found"));
        String jwtoken = jsonWebToken.generateTokens(user);
        return new AuthResponse(jwtoken,user.getUsername());
    }
}
