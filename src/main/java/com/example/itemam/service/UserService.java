package com.example.itemam.service;

import com.example.itemam.entity.Role;
import com.example.itemam.entity.User;
import com.example.itemam.repository.UserRepository;
import com.example.itemam.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepository.findAll();
    }


    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    public boolean isValidData(String email, String password) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        return byEmail.isPresent() && passwordEncoder.matches(password,byEmail.get().getPassword());
    }

    public CurrentUser getCurrentUser(String email){
        Optional<User> byEmail = userRepository.findByEmail(email);
        if(!byEmail.isPresent()){
            return null;
        }
        return new CurrentUser(byEmail.get());
    }
}
