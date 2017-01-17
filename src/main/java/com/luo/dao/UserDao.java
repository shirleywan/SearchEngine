package com.luo.dao;

import com.luo.domain.User;

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
