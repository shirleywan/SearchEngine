package com.szl.service;

import com.szl.baseTest.SpringTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zsc on 2017/1/18.
 */
public class QuestionSearchServiceTest extends SpringTestCase {
    @Autowired
    private QuestionSearchService questionSearchService;

    @Test
    public void selectUserByIdTest(){
        questionSearchService.test();
        questionSearchService.getAllID("question", "阿里");
    }
}
