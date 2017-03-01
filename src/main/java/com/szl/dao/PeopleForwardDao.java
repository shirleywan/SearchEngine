package com.szl.dao;

import com.szl.domain.Forward;

import java.util.List;
import java.util.Map;

/**
 * Created by zsc on 2016/12/22.
 */
public interface PeopleForwardDao {
    Forward selectById(int Id);

    List<Forward> selectAll();

    Long getPageCounts(List<Integer> Ids);

    List<Forward> selectByPage(List<Integer> Ids);

    List<Forward> selectByMap(Map<String, Object> map);

    void insert(Forward forward);

    void update(Forward forward);

    void delete(int Id);
}
