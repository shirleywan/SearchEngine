package com.szl.dao;

import com.szl.domain.Forward;

import java.util.List;

/**
 * Created by zsc on 2016/12/22.
 */
public interface TopicForwardDao {
    Forward selectById(int Id);

    List<Forward> selectAll();

    Long getPageCounts(List<Integer> Ids);

    List<Forward> selectByPage(List<Integer> Ids);

    void insert(Forward forward);

    void update(Forward forward);

    void delete(int Id);
}
