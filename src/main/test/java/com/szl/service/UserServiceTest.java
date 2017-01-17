package com.szl.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.szl.baseTest.SpringTestCase;
import com.szl.domain.User;
/**
 * Created by zsc on 2017/1/15.
 */
public class UserServiceTest extends SpringTestCase {

    @Autowired
    private UserService userService;

    @Test
    public void selectUserByIdTest(){
        User user = userService.selectUserById(1);
        System.out.println(user.getUserName() + ":" + user.getUserPassword());
    }
}
