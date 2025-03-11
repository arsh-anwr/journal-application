package com.expense.tracker.controller;

import com.expense.tracker.entity.User;
import com.expense.tracker.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable ObjectId id) {
        User userEntity = userService.findById(id);

        if (userEntity == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userEntity, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable ObjectId id) {
        userService.deleteById(id);
        return true;
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User oldUser = userService.findByUserId(username);

        if (oldUser != null) {
            oldUser.setPassword(user.getPassword() != null && !user.getPassword().equals("") ? user.getPassword() : oldUser.getPassword());
            userService.saveEntry(oldUser);

            return new ResponseEntity<>(oldUser, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}
