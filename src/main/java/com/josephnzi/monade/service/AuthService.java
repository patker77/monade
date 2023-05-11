package com.josephnzi.monade.service;

import com.josephnzi.monade.dto.RegisterRequest;
import com.josephnzi.monade.exception.EmailException;
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
import java.util.Date;
import java.util.UUID;

@Service
@AllArgsConstructor

public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;

    @Transactional
    public void signup(RegisterRequest registerRequest) throws EmailException {
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
        verificationTokenRepository.save(verificationToken);
        return token;
    }
}
