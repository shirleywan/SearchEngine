package com.szl.controller;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import com.szl.Config;
import com.szl.Filter;
import com.szl.domain.Forward;
import com.szl.domain.Reverse;
import com.szl.service.QuestionSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Created by zsc on 2017/1/18.
 */
@Controller
public class QuestionController {

    @Autowired
    private QuestionSearchService questionSearchService;

    @RequestMapping("/search")
    public String search() {
        return "searchForm";
    }

    //@RequestParam(value="q")中value要和jsp的input的name(只有name，id无所谓)相同，方法为get，post不显示
    @RequestMapping("/question")
    public ModelAndView getQuestions(@RequestParam(value="q") String questionStr){
        ModelAndView mav = new ModelAndView("questionForm");
        List<Forward> forwards = genUrls(questionStr, questionSearchService.getfQuestionsMap(),  questionSearchService.getrQuestionsMap());
        mav.addObject("forwards", forwards);
        return mav;
    }

    private List<Forward> genUrls(String str, Map<String, Forward> fQuestionsMap, Map<String, Reverse> rQuestionsMap) {
        //得到按序排列的关键字集合
        List<Term> terms = Filter.accept(HanLP.segment(str));
        for (int i = 0; i < terms.size(); i++) {
            System.out.println("分词结果 " + terms.get(i).word);
        }
        List<Forward> forwards = new ArrayList<Forward>();
        List<Reverse> keyWords = new ArrayList<Reverse>();
        for (Term term : terms) {
            if (rQuestionsMap.containsKey(term.word)) {
                keyWords.add(rQuestionsMap.get(term.word));
            }
        }
        Collections.sort(keyWords);//按IDF大小排序

        //得到按序排列的url集合，只要string
        List<String> sortedUrls = new ArrayList<String>();
        for (Reverse reverse : keyWords) {
            sortedUrls.add(reverse.getUrls());
        }

        //得到最终排序
        Set<String> urls = new LinkedHashSet<>();//保证按顺序且不重复
        List<String> temp = new ArrayList<String>();
        int len = sortedUrls.size() + 1;
        for (int i = len - 1; i != 0; i--) {
            genInSequence(urls, sortedUrls, 0, i, temp);
        }

        //显示结果
        System.out.println("最终url: " + urls);
        for (String num : urls) {
            if (fQuestionsMap.containsKey(num)) {
                forwards.add(fQuestionsMap.get(num));
            }
        }
        for (int i = 0; i < forwards.size(); i++) {
            System.out.println("最终结果  " + forwards.get(i).getId());
        }
        return forwards;
    }

    private void genInSequence(Set<String> urls, List<String> sortedUrls, int start, int len, List<String> temp) {//len为组合的长度
        if (len == 0) {
            List<String> result = new ArrayList<>();
            result.addAll(Arrays.asList(temp.get(0).split(Config.DELIMITER)));
            for (int i = 1; i < temp.size(); i++) {
                result.retainAll(Arrays.asList(temp.get(i).split(Config.DELIMITER)));
            }
            urls.addAll(result);
            return;
        }
        if (start == sortedUrls.size()) {
            return;
        }
        temp.add(sortedUrls.get(start));
        genInSequence(urls, sortedUrls, start + 1, len - 1, temp);
        temp.remove(temp.size() - 1);
        genInSequence(urls, sortedUrls, start + 1, len, temp);
    }
}
