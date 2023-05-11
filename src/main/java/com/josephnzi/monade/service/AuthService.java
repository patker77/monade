package com.josephnzi.monade.service;

import com.josephnzi.monade.dto.RegisterRequest;
import com.josephnzi.monade.exception.AccountVerificationException;
import com.josephnzi.monade.exception.MonadeExceptions;
import com.josephnzi.monade.exception.UsernameNotFoundExceptionptions;
import com.josephnzi.monade.model.NotificationEmail;
import com.josephnzi.monade.model.User;
import com.josephnzi.monade.model.VerificationToken;
import com.josephnzi.monade.repository.UserRepository;
import com.josephnzi.monade.repository.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
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

    @Transactional
    public void signup(RegisterRequest registerRequest) throws MonadeExceptions {
        User user = new User();
                user.setUserName(registerRequest.getUsername());
                user.setName(registerRequest.getName());
                user.setEmail(registerRequest.getEmail());
                user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
                user.setCreated(Instant.now());
                user.setEnabled(false);
                userRepository.save(user);
                String token = generateUserToken(user);
                mailService.sendEmail(new NotificationEmail("Please verify your email Account ","Thank you for your registration at Monade. Please Click on the link below to verify your account "+
                        "http://locahost:8080/api/auth/accountVerification/"+token,user.getEmail()));

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
        String username = verificationToken.getUser().getUserName();
        User user =userRepository.findByUserName(username).orElseThrow(() -> new UsernameNotFoundExceptionptions("User with the username: "+username +" not found"));
        user.setEnabled(true);
        userRepository.save(user);
    }

}
