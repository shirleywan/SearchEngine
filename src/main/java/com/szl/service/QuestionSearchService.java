package com.szl.service;

import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import com.szl.Filter;
import com.szl.dao.*;
import com.szl.domain.Forward;
import com.szl.domain.Reverse;
import org.jsoup.select.Evaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zsc on 2017/1/18.
 * 构造方法在@PostConstruct前执行
 * fQuestions和rQuestions是为了构建fQuestionsMap和rQuestionsMap，rQuestionsMap是用来查询，最终得到有序的结果url序号，
 * 然后通过查询fQuestionsMap得到最终的url信息，可以将rQuestionsMap和fQuestionsMap整合，节省空间
 */
@Service
public class QuestionSearchService implements SearchService {
    private List<Forward> fQuestions;
    private List<Reverse> rQuestions;
    private Map<String, Reverse> rQuestionsMap = new HashMap<String, Reverse>();
    private Map<String, Forward> fQuestionsMap = new HashMap<String, Forward>();

    private List<Forward> fPeoples;
    private List<Reverse> rPeoples;
    private Map<String, Reverse> rPeoplesMap = new HashMap<String, Reverse>();
    private Map<String, Forward> fPeoplesMap = new HashMap<String, Forward>();

    private List<Forward> fTopics;
    private List<Reverse> rTopics;
    private Map<String, Reverse> rTopicsMap = new HashMap<String, Reverse>();
    private Map<String, Forward> fTopicsMap = new HashMap<String, Forward>();

    @Autowired
    private QuestionForwardDao questionForwardDao;

    @Autowired
    private QuestionReverseDao questionReverseDao;

    @Autowired
    private PeopleForwardDao peopleForwardDao;

    @Autowired
    private PeopleReverseDao peopleReverseDao;

    @Autowired
    private TopicForwardDao topicForwardDao;

    @Autowired
    private TopicReverseDao topicReverseDao;


    @PostConstruct
    public void init() {
        System.out.println("@PostConstruct方法被调用");
        fQuestions = getFQuestions();
        rQuestions = getRQuestions();//将数据库的问题正排和倒排读进来
        fPeoples = getFPeoples();
        rPeoples = getRPeoples();
        fTopics = getFTopics();
        rTopics = getRTopics();
        //将2个表读到Map里
        for (Reverse question : rQuestions) {
            rQuestionsMap.put(question.getKeyWords(), question);
        }

        for (Forward question : fQuestions) {
            fQuestionsMap.put(String.valueOf(question.getId()), question);
        }
        System.out.println(rQuestionsMap.size() + "     " + fQuestionsMap.size());

        for (Reverse people : rPeoples) {
            rPeoplesMap.put(people.getKeyWords(), people);
        }

        for (Forward people : fPeoples) {
            fPeoplesMap.put(String.valueOf(people.getId()), people);
        }
        System.out.println(rPeoplesMap.size() + "     " + fPeoplesMap.size());

        for (Reverse people : rTopics) {
            rTopicsMap.put(people.getKeyWords(), people);
        }

        for (Forward people : fTopics) {
            fTopicsMap.put(String.valueOf(people.getId()), people);
        }
        System.out.println(rTopicsMap.size() + "     " + fTopicsMap.size());

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

    public Map<String, Forward> getfPeoplesMap() {
        return fPeoplesMap;
    }

    public Map<String, Reverse> getrPeoplesMap() {
        return rPeoplesMap;
    }

    private List<Forward> getFPeoples() {
        return peopleForwardDao.selectAll();
    }

    private List<Reverse> getRPeoples() {
        return peopleReverseDao.selectAll();
    }

    public Map<String, Forward> getfTopicsMap() {
        return fTopicsMap;
    }

    public Map<String, Reverse> getrTopicsMap() {
        return rTopicsMap;
    }

    private List<Forward> getFTopics() {
        return topicForwardDao.selectAll();
    }

    private List<Reverse> getRTopics() {
        return topicReverseDao.selectAll();
    }

    public Long getQPageCounts(List<Integer> Ids) {
        return questionForwardDao.getPageCounts(Ids);
    }

    public List<Forward> selectQByPage(List<Integer> Ids) {
        return questionForwardDao.selectByPage(Ids);
    }

    public Long getPPageCounts(List<Integer> Ids) {
        return peopleForwardDao.getPageCounts(Ids);
    }

    public List<Forward> selectPByPage(List<Integer> Ids) {
        return peopleForwardDao.selectByPage(Ids);
    }

    public Long getTPageCounts(List<Integer> Ids) {
        return topicForwardDao.getPageCounts(Ids);
    }

    public List<Forward> selectTByPage(List<Integer> Ids) {
        return topicForwardDao.selectByPage(Ids);
    }

}
