
package com.expense.tracker.service;

import com.expense.tracker.entity.User;
import com.expense.tracker.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserService {

    @Autowired
    UserRepository userRepository;

    public void saveEntry(User user) {
        userRepository.save(user);
    }

    public void saveNewUser(User user) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of("USER"));
        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(ObjectId id) {
        return userRepository.findById(id).orElse(null);
    }

    public User findByUserId(String id) {
        return userRepository.findByUserName(id);
    }

    public void deleteById(ObjectId id) {
        userRepository.deleteById(id);
    }
}
