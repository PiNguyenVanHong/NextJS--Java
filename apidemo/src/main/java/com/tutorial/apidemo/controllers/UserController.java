package com.tutorial.apidemo.controllers;

import com.tutorial.apidemo.models.CustomUserDetails;
import com.tutorial.apidemo.models.ResponseObject;
import com.tutorial.apidemo.models.User;
import com.tutorial.apidemo.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RequestMapping("${api.prefix}/users")
@RestController
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ResponseObject> authenticatedUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "", currentUser)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ResponseObject("unauthorized", e.getMessage(), null)
            );
        }
    }

    @GetMapping("/")
    public ResponseEntity<ResponseObject> handleAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "", users)
        );
    }
}
