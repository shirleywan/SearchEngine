package com.szl.dao;

import com.szl.domain.Reverse;

import java.util.List;

/**
 * Created by zsc on 2016/12/22.
 */
public interface QuestionReverseDao {
    Reverse selectById(int Id);

    Reverse selectByKeyWords(String keywords);

    List<Reverse> selectAll();

    void insert(Reverse reverse);

    void update(Reverse reverse);

    void delete(int Id);
}
