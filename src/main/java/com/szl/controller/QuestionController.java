package com.szl.controller;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.szl.page.BaseController;
import com.szl.Config;
import com.szl.Filter;
import com.szl.page.Page;
import com.szl.domain.Forward;
import com.szl.domain.Reverse;
import com.szl.service.QuestionSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by zsc on 2017/1/18.
 */
@Controller
@SessionAttributes("forwards")
public class QuestionController extends BaseController {

    @Autowired
    private QuestionSearchService questionSearchService;

    @RequestMapping("/")
    public String search() {
        return "searchForm";
    }

    //@RequestParam(value="q")中value要和jsp的input的name(只有name，id无所谓)相同，方法为get，post不显示
    @RequestMapping("/search")
    public ModelAndView getQuestions(HttpServletRequest request, @RequestParam(value = "type") String type, @RequestParam(value = "q") String questionStr) {
        ModelAndView mav;
        List<Forward> forwards;
        List<Integer> allIds;
        List<Integer> everyIds = new ArrayList<Integer>();
        Long totalCount;
        Page page;
//        if (type.equals("question")) {
//            forwards = genUrls(type, questionStr, questionSearchService.getfQuestionsMap(), questionSearchService.getrQuestionsMap());
//            mav = new ModelAndView("questionsResult");
//        } else if (type.equals("people")) {
//            forwards = genUrls(type, questionStr, questionSearchService.getfPeoplesMap(), questionSearchService.getrPeoplesMap());
//            mav = new ModelAndView("peoplesResult");
//        } else {
//            forwards = genUrls(type, questionStr, questionSearchService.getfTopicsMap(),  questionSearchService.getrTopicsMap());
//            mav = new ModelAndView("topicsResult");
//        }

        if (type.equals("question")) {
            allIds = genIds(type, questionStr, questionSearchService.getfQuestionsMap(), questionSearchService.getrQuestionsMap());
            totalCount = questionSearchService.getQPageCounts(allIds);
            //设置分页对象
            page = executePage(request, totalCount);
            for (long i = page.getBeginIndex(); i < (Math.min(page.getEndinIndex(), allIds.size())); i++) {
                everyIds.add(allIds.get((int) i));
            }
            forwards = questionSearchService.selectQByPage(everyIds);
            mav = new ModelAndView("questionsResult");
        } else if (type.equals("people")) {
            allIds = genIds(type, questionStr, questionSearchService.getfPeoplesMap(), questionSearchService.getrPeoplesMap());
            totalCount = questionSearchService.getPPageCounts(allIds);
            //设置分页对象
            page = executePage(request, totalCount);
            for (long i = page.getBeginIndex(); i < (Math.min(page.getEndinIndex(), allIds.size())); i++) {
                everyIds.add(allIds.get((int) i));
            }
            forwards = questionSearchService.selectPByPage(everyIds);
            mav = new ModelAndView("peoplesResult");
        } else {
            allIds = genIds(type, questionStr, questionSearchService.getfTopicsMap(), questionSearchService.getrTopicsMap());
            totalCount = questionSearchService.getTPageCounts(allIds);
            //设置分页对象
            page = executePage(request, totalCount);
            for (long i = page.getBeginIndex(); i < (Math.min(page.getEndinIndex(), allIds.size())); i++) {
                everyIds.add(allIds.get((int) i));
            }
            forwards = questionSearchService.selectTByPage(everyIds);
            mav = new ModelAndView("topicsResult");
        }

        mav.addObject("q", questionStr);
        mav.addObject("forwards", forwards);
        return mav;
    }


    private List<Integer> genIds(String type, String str, Map<String, Forward> fQuestionsMap, Map<String, Reverse> rQuestionsMap) {
        //得到按序排列的关键字集合
        List<Term> terms = Filter.accept(HanLP.segment(str));
        for (int i = 0; i < terms.size(); i++) {
            System.out.println("分词结果 " + terms.get(i).word);
        }
        List<Integer> ids = new ArrayList<Integer>();
        List<Reverse> keyWords = new ArrayList<Reverse>();
        //此处有区别，使用type区分
        if (type.equals("question")) {
            for (Term term : terms) {
                if (rQuestionsMap.containsKey(term.word)) {
                    keyWords.add(rQuestionsMap.get(term.word));
                }
            }
        } else {
            for (Term term : terms) {
                for (Map.Entry<String, Reverse> entry : rQuestionsMap.entrySet()) {
                    if (entry.getKey().contains(term.word)) {
                        keyWords.add(entry.getValue());
                    }
                }
            }
        }

        Collections.sort(keyWords);//按IDF大小排序

        //得到按序排列的url集合，只要string
        List<String> sortedUrls = new ArrayList<String>();
        for (Reverse reverse : keyWords) {
            sortedUrls.add(reverse.getUrls());
        }

        //得到最终排序
        Set<String> urls = new LinkedHashSet<String>();//保证按顺序且不重复
        List<String> temp = new ArrayList<String>();
        int len = sortedUrls.size() + 1;
        for (int i = len - 1; i != 0; i--) {
            genInSequence(urls, sortedUrls, 0, i, temp);
        }

        //显示结果
        System.out.println("最终url: " + urls);
        for (String url : urls) {
            ids.add(Integer.parseInt(url));

        }

        return ids;
    }

    private List<Forward> genUrls(String type, String str, Map<String, Forward> fQuestionsMap, Map<String, Reverse> rQuestionsMap) {
        //得到按序排列的关键字集合
        List<Term> terms = Filter.accept(HanLP.segment(str));
        for (int i = 0; i < terms.size(); i++) {
            System.out.println("分词结果 " + terms.get(i).word);
        }
        List<Forward> forwards = new ArrayList<Forward>();
        List<Reverse> keyWords = new ArrayList<Reverse>();
        //此处有区别，使用type区分
        if (type.equals("question")) {
            for (Term term : terms) {
                if (rQuestionsMap.containsKey(term.word)) {
                    keyWords.add(rQuestionsMap.get(term.word));
                }
            }
        } else {
            for (Term term : terms) {
                for (Map.Entry<String, Reverse> entry : rQuestionsMap.entrySet()) {
                    if (entry.getKey().contains(term.word)) {
                        keyWords.add(entry.getValue());
                    }
                }
            }
        }

        Collections.sort(keyWords);//按IDF大小排序

        //得到按序排列的url集合，只要string
        List<String> sortedUrls = new ArrayList<String>();
        for (Reverse reverse : keyWords) {
            sortedUrls.add(reverse.getUrls());
        }

        //得到最终排序
        Set<String> urls = new LinkedHashSet<String>();//保证按顺序且不重复
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
