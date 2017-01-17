package com.szl.service;

import com.szl.dao.UserDao;
import com.szl.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zsc on 2017/1/15.
 */
@Service
public class UserServiceImpl  implements UserService{
    @Autowired
    private UserDao userDao;

    public User selectUserById(Integer userId) {
        return userDao.selectUserById(userId);

    }
}
