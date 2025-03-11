package com.expense.tracker.userTest;

import com.expense.tracker.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest public class UserServiceTests {

    @Autowired
    UserService userService;

    @Test
    public void testFindByUserName() {
        assertNotNull(userService.findByUserId("arshanwr"), "user should be not null");
    }
}
