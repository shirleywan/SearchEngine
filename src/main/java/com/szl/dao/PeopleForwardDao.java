package com.szl.dao;

import com.szl.domain.Forward;

import java.util.List;

/**
 * Created by zsc on 2016/12/22.
 */
public interface PeopleForwardDao {
    Forward selectById(int Id);

    List<Forward> selectAll();

    void insert(Forward forward);

    void update(Forward forward);

    void delete(int Id);
}
