package com.szl.dao;

import com.szl.domain.User;

/**
 * Created by zsc on 2017/1/15.
 */
public interface UserDao {

    /**
     * @param userId
     * @return User
     */
    public User selectUserById(Integer userId);

}
