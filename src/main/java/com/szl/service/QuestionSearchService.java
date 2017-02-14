package com.szl.service;

import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import com.szl.Filter;
import com.szl.dao.QuestionForwardDao;
import com.szl.dao.QuestionReverseDao;
import com.szl.domain.Forward;
import com.szl.domain.Reverse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zsc on 2017/1/18.
 * 构造方法在@PostConstruct前执行

 */
@Service
public class QuestionSearchService implements SearchService {
    private List<Forward> fQuestions;
    private List<Reverse> rQuestions;
    private Map<String, Reverse> rQuestionsMap = new HashMap<String, Reverse>();
    private Map<String, Forward> fQuestionsMap = new HashMap<String, Forward>();

    @Autowired
    private QuestionForwardDao questionForwardDao;

    @Autowired
    private QuestionReverseDao questionReverseDao;

    @PostConstruct
    public void init() {
        System.out.println("@PostConstruct方法被调用");
        fQuestions = getFQuestions();
        rQuestions = getRQuestions();//将数据库的问题正排和倒排读进来
        //将2个表读到Map里
        for (Reverse question : rQuestions) {
            rQuestionsMap.put(question.getKeyWords(), question);
        }

        for (Forward question : fQuestions) {
            fQuestionsMap.put(String.valueOf(question.getId()), question);
        }
        System.out.println(rQuestionsMap.size() + "     " + fQuestionsMap.size());

    }

    public void test() {
        List<Term> terms = Filter.accept(StandardTokenizer.segment("阿里巴巴体验"));
        for (int i = 0; i < terms.size(); i++) {
            System.out.println("分词结果 " + terms.get(i).word);
        }
    }

    public Map<String, Forward> getfQuestionsMap() {
        return fQuestionsMap;
    }

    public Map<String, Reverse> getrQuestionsMap() {
        return rQuestionsMap;
    }

    private List<Forward> getFQuestions() {
        return questionForwardDao.selectAll();
    }

    private List<Reverse> getRQuestions() {
        return questionReverseDao.selectAll();
    }

}
