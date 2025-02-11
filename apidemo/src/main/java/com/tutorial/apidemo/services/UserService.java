package com.tutorial.apidemo.services;

import com.tutorial.apidemo.models.CustomUserDetails;
import com.tutorial.apidemo.models.User;
import com.tutorial.apidemo.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> user = this.userRepository.findByEmail(username);

        if(user.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }

        return new CustomUserDetails(user.get());
    }

    public CustomUserDetails loadUserById(Long id) {
        Optional<User> user = this.userRepository.findById(id);

        if(user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        return new CustomUserDetails(user.get());
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        this.userRepository.findAll().forEach(users::add);
        return users;
    }
}
