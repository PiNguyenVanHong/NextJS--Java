package com.tutorial.apidemo.controllers;

import com.tutorial.apidemo.components.JwtTokenUtils;
import com.tutorial.apidemo.dtos.LoginUserDto;
import com.tutorial.apidemo.dtos.RegisterUserDto;
import com.tutorial.apidemo.dtos.VerifyUserDto;
import com.tutorial.apidemo.exceptions.NotFoundException;
import com.tutorial.apidemo.models.CustomUserDetails;
import com.tutorial.apidemo.models.ResponseObject;
import com.tutorial.apidemo.models.User;
import com.tutorial.apidemo.responses.LoginResponse;
import com.tutorial.apidemo.services.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RequestMapping("${api.prefix}/auth")
@RestController
public class AuthenticationController {
    private final JwtTokenUtils jwtTokenUtils;

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<ResponseObject> register(@Valid @RequestBody RegisterUserDto registerUserDto) {
        User user = authenticationService.signup(registerUserDto);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Registered successfully", null)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseObject> login(@Valid @RequestBody LoginUserDto loginUserDto, HttpServletResponse response) {
        User user = authenticationService.authenticate(loginUserDto);
        String token = jwtTokenUtils.generateToken(new CustomUserDetails(user));
        LoginResponse loginResponse = new LoginResponse(token, user.getName());

        Cookie cookie = new Cookie("JWT", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Login successfully", loginResponse)
        );
    }

    @PostMapping("/verify")
    public ResponseEntity<ResponseObject> verifyUser(@Valid @RequestBody VerifyUserDto verifyUserDto) {
        authenticationService.verifyUser(verifyUserDto);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Account Verify successfully", null)
        );
    }

    @PostMapping("/resend")
    public ResponseEntity<ResponseObject> resendVerificationCode(@RequestParam String email) {

        try {
            authenticationService.resendVerificationCode(email);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Please check your email!!!", null)
            );
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("failed", "Your email is not exist", null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("failed", "Something went wrong", null)
            );
        }
    }
}
