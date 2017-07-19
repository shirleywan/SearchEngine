package com.szl.service;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import com.szl.page.Page;
import com.szl.util.Filter;
import com.szl.dao.*;
import com.szl.domain.Forward;
import com.szl.domain.Reverse;
import com.szl.util.PageNumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
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
//    private List<Forward> fQuestions;
//    private List<Reverse> rQuestions;
//    private Map<String, Reverse> rQuestionsMap = new HashMap<String, Reverse>();
//    private Map<String, Forward> fQuestionsMap = new HashMap<String, Forward>();
//
//    private List<Forward> fPeoples;
//    private List<Reverse> rPeoples;
//    private Map<String, Reverse> rPeoplesMap = new HashMap<String, Reverse>();
//    private Map<String, Forward> fPeoplesMap = new HashMap<String, Forward>();
//
//    private List<Forward> fTopics;
//    private List<Reverse> rTopics;
//    private Map<String, Reverse> rTopicsMap = new HashMap<String, Reverse>();
//    private Map<String, Forward> fTopicsMap = new HashMap<String, Forward>();

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


    /*@PostConstruct
    public void init() {
        System.out.println("@PostConstruct方法被调用");
        fQuestions = getFQuestions();
        rQuestions = getRQuestions();//将数据库的问题正排和倒排读进来
        fPeoples = getFPeoples();
        rPeoples = getRPeoples();
        fTopics = getFTopics();
        rTopics = getRTopics();
        将2个表读到Map里
        for (Reverse question : rQuestions) {
            rQuestionsMap.put(question.getKeyWords(), question);
        }

        for (Forward question : fQuestions) {
            fQuestionsMap.put(String.valueOf(question.getId()), question);
        }
        System.out.println("问题倒排列表 " + rQuestionsMap.size() + "     " + "问题正排列表 " +  fQuestionsMap.size());

        for (Reverse people : rPeoples) {
            rPeoplesMap.put(people.getKeyWords(), people);
        }

        for (Forward people : fPeoples) {
            fPeoplesMap.put(String.valueOf(people.getId()), people);
        }
        System.out.println("用户倒排列表 " + rPeoplesMap.size() + "     " + "用户正排列表 " + fPeoplesMap.size());

        for (Reverse people : rTopics) {
            rTopicsMap.put(people.getKeyWords(), people);
        }

        for (Forward people : fTopics) {
            fTopicsMap.put(String.valueOf(people.getId()), people);
        }
        System.out.println("话题倒排列表 " + rTopicsMap.size() + "     " + "话题正排列表 " + fTopicsMap.size());

    }*/

    public void test() {
//        HanLP.Config.ShowTermNature = true;

//        List<Term> terms = Filter.accept(StandardTokenizer.segment(""));
        String str = "";
        List<Term> terms = Filter.accept(StandardTokenizer.segment(str));
//        List<Term> terms2 =StandardTokenizer.segment("");
        List<Term> terms2 =StandardTokenizer.segment(str);
        System.out.println(terms.toString());
        System.out.println(terms2.toString());
    }

//    public Map<String, Forward> getfQuestionsMap() {
//        return fQuestionsMap;
//    }
//
//    public Map<String, Reverse> getrQuestionsMap() {
//        return rQuestionsMap;
//    }

//    public Map<String, Forward> getfPeoplesMap() {
//        return fPeoplesMap;
//    }
//
//    public Map<String, Reverse> getrPeoplesMap() {
//        return rPeoplesMap;
//    }
//
//    private List<Forward> getFPeoples() {
//        return peopleForwardDao.selectAll();
//    }
//
//    private List<Reverse> getRPeoples() {
//        return peopleReverseDao.selectAll();
//    }
//
//    public Map<String, Forward> getfTopicsMap() {
//        return fTopicsMap;
//    }
//
//    public Map<String, Reverse> getrTopicsMap() {
//        return rTopicsMap;
//    }

    private List<Forward> getFQuestions() {
        return questionForwardDao.selectAll();
    }

    private List<Reverse> getRQuestions() {
        return questionReverseDao.selectAll();
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

    public Reverse getQUrls(String keywords) {
        return questionReverseDao.selectByKeyWords(keywords);
    }

    public Reverse getPUrls(String keywords) {
        return peopleReverseDao.selectByKeyWords(keywords);
    }

    public Reverse getTUrls(String keywords) {
        return topicReverseDao.selectByKeyWords(keywords);
    }

    /**
     * use Util
     * @param type 类型
     * @param questionStr 内容
     * @return list
     */
    public List<List<Integer>> getAllID(String type, String questionStr) {
        List<List<Integer>> list = new ArrayList<List<Integer>>();
        List<Reverse> keyWords=getAllReverse(type, questionStr);
        PageNumUtil.genIds(type, keyWords, list);
        return list;
    }

    //获取所有倒排列表
    private List<Reverse> getAllReverse(String type, String questionStr) {
        System.out.println(questionStr);
        //得到按序排列的关键字集合
        List<Term> terms = Filter.accept(StandardTokenizer.segment(questionStr));
        System.out.println("分词结果 " + terms.toString());
        List<Reverse> keyWords = new ArrayList<Reverse>();
        Reverse key;
        for (Term term : terms) {
            if (type.equals("question")) {
                key = getQUrls(term.word);
            } else if (type.equals("people")) {
                key = getPUrls(term.word);
            } else {
                key = getTUrls(term.word);
            }


            if (key != null) {
                keyWords.add(key);
            }
        }
        return keyWords;
    }

    //生成左边ID
    public List<Integer> getLeftPageID(Page page, List<Integer> leftID) {
        List<Integer> leftPageID = new ArrayList<Integer>();
        for (long i = page.getBeginIndex(); i < (Math.min(page.getEndinIndex(), leftID.size())); i++) {
            leftPageID.add(leftID.get((int) i));
        }
        return leftPageID;
    }

    //生成右边ID
    public List<Integer> getLRightPageID(Page page, List<Integer> rightID) {
        List<Integer> rightPageID = new ArrayList<Integer>();
        for (long i = page.getBeginIndex(); i < (Math.min(page.getEndinIndex(), rightID.size())); i++) {
            rightPageID.add(rightID.get((int) i));
        }
        return rightPageID;
    }


    //分页插件
    public List<Forward> selectQByMap(Map<String, Object> map) {
        return questionForwardDao.selectByMap(map);
    }

    public List<Forward> selectPByMap(Map<String, Object> map) {
        return peopleForwardDao.selectByMap(map);
    }

    public List<Forward> selectTByMap(Map<String, Object> map) {
        return topicForwardDao.selectByMap(map);
    }

}
