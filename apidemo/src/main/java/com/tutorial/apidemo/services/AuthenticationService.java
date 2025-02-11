package com.tutorial.apidemo.services;

import com.tutorial.apidemo.exceptions.CustomException;
import com.tutorial.apidemo.dtos.LoginUserDto;
import com.tutorial.apidemo.dtos.RegisterUserDto;
import com.tutorial.apidemo.dtos.VerifyUserDto;
import com.tutorial.apidemo.exceptions.NotFoundException;
import com.tutorial.apidemo.exceptions.UnauthorizedException;
import com.tutorial.apidemo.models.User;
import com.tutorial.apidemo.repositories.UserRepository;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@AllArgsConstructor
@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final EmailServiceImpl emailService;

    public User signup(RegisterUserDto input) {
        this.userRepository.findByEmail(input.getEmail())
                .ifPresent(user -> { throw new UnauthorizedException("Your email is exist"); });

        User user = new User(
                input.getName(),
                input.getEmail(),
                this.passwordEncoder.encode(input.getPassword())
        );

        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));

        userRepository.save(user);
        sendVerificationEmail(user);
        return user;
    }

    public User authenticate(LoginUserDto input) {
        User user = this.userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new NotFoundException("Your email is not exist"));

        if(!user.isActive()) {
            throw new CustomException("Account does not active");
        }

        if(!this.passwordEncoder.matches(input.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Your password is incorrect");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return user;
    }

    public void verifyUser(VerifyUserDto input) {
        Optional<User> user = this.userRepository.findByEmail(input.getEmail());

        if(user.isEmpty()) {
            throw new NotFoundException("Your email not found");
        }

        if(user.get().getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
            throw new CustomException("Verification code expired");
        }

        if(user.get().getVerificationCode().equals(input.getVerificationCode())) {
            user.get().setActive(true);
            user.get().setVerificationCode(null);
            user.get().setVerificationCodeExpiresAt(null);
            userRepository.save(user.get());
        } else {
            throw new CustomException("Invalid verification code");
        }
    }

    public void resendVerificationCode(String email) {
        Optional<User> user = this.userRepository.findByEmail(email);
        if(user.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        if(user.get().isActive()) {
            throw new UnauthorizedException("Your account is already verified");
        }
        user.get().setVerificationCode(generateVerificationCode());
        user.get().setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1));
        sendVerificationEmail(user.get());
        userRepository.save(user.get());
    }

    public void sendVerificationEmail(User user) {
        String subject = "Verification Code";
        String verificationCode = user.getVerificationCode();

        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}
