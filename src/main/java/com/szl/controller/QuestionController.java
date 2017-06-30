package com.szl.controller;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import com.szl.Config;
import com.szl.Filter;
import com.szl.page.Page;
import com.szl.domain.Forward;
import com.szl.domain.Reverse;
import com.szl.page.PageUtil;
import com.szl.service.QuestionSearchService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by zsc on 2017/1/18.
 */
@Controller
@SessionAttributes("forwards")
public class QuestionController {

    private HashMap<String, List<Integer>> cache = new HashMap<String, List<Integer>>();

    @Autowired
    private QuestionSearchService questionSearchService;

    @RequestMapping("/")
    public String search() {
        return "searchForm";
    }

//    @RequestMapping("/search")
//    public ModelAndView getQuestions(HttpServletRequest request, @RequestParam(value = "type") String type, @RequestParam(value = "q") String questionStr) {
//        ModelAndView mav;
//        List<Forward> forwards;
//        List<Integer> allIds;
//        List<Integer> everyIds = new ArrayList<Integer>();
//        Long totalCount = -1l;
//        Page page;
//        Map<String, Object> map = new HashMap<String, Object>();
//
//        if (type.equals("question")) {
//            if (cache.containsKey(questionStr)) {
//                allIds = cache.get(questionStr);
//                System.out.println("从缓存里取");
//            } else {
//                allIds = genIds(type, questionStr, questionSearchService.getfQuestionsMap(), questionSearchService.getrQuestionsMap());
//                cache.put(questionStr, allIds);
//                System.out.println("重新计算");
//            }
////            totalCount = questionSearchService.getQPageCounts(allIds);
//            //设置分页对象
//            page = PageUtil.executePage(request, totalCount);
//            for (long i = page.getBeginIndex(); i < (Math.min(page.getEndinIndex(), allIds.size())); i++) {
//                everyIds.add(allIds.get((int) i));
//            }
//            page.setAllIds(allIds);
//            page.setEveryIds(everyIds);
//            map.put("page", page);
//            map.put("request", request);
//            forwards = questionSearchService.selectQByMap(map);
//            mav = new ModelAndView("questionsResult");
//        } else if (type.equals("people")) {
//            if (cache.containsKey(questionStr)) {
//                allIds = cache.get(questionStr);
//                System.out.println("从缓存里取");
//            } else {
//                allIds = genIds(type, questionStr, questionSearchService.getfPeoplesMap(), questionSearchService.getrPeoplesMap());
//                cache.put(questionStr, allIds);
//                System.out.println("重新计算");
//            }
////            totalCount = questionSearchService.getPPageCounts(allIds);
//            //设置分页对象
//            page = PageUtil.executePage(request, totalCount);
//            for (long i = page.getBeginIndex(); i < (Math.min(page.getEndinIndex(), allIds.size())); i++) {
//                everyIds.add(allIds.get((int) i));
//            }
//            page.setAllIds(allIds);
//            page.setEveryIds(everyIds);
//            map.put("page", page);
//            map.put("request", request);
//            forwards = questionSearchService.selectPByMap(map);
//            mav = new ModelAndView("peoplesResult");
//        } else {
//            if (cache.containsKey(questionStr)) {
//                allIds = cache.get(questionStr);
//                System.out.println("从缓存里取");
//            } else {
//                allIds = genIds(type, questionStr, questionSearchService.getfTopicsMap(), questionSearchService.getrTopicsMap());
//                cache.put(questionStr, allIds);
//                System.out.println("重新计算");
//            }
////            totalCount = questionSearchService.getTPageCounts(allIds);
//            //设置分页对象
//            page = PageUtil.executePage(request, totalCount);
//            for (long i = page.getBeginIndex(); i < (Math.min(page.getEndinIndex(), allIds.size())); i++) {
//                everyIds.add(allIds.get((int) i));
//            }
//            page.setAllIds(allIds);
//            page.setEveryIds(everyIds);
//            map.put("page", page);
//            map.put("request", request);
//            forwards = questionSearchService.selectTByMap(map);
//            mav = new ModelAndView("topicsResult");
//        }
//
//        mav.addObject("q", questionStr);
//        mav.addObject("forwards", forwards);
//        return mav;
//    }


    /**
     * 不使用插件，注释mybatis-config代码
     *
     * @param request
     * @param type
     * @param questionStr
     * @return
     * @RequestParam(value="q")中value要和jsp的input的name(只有name，id无所谓)相同，方法为get，post不显示
     */
    @RequestMapping("/search")
    public ModelAndView getQuestions(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "type") String type, @RequestParam(value = "q") String questionStr) {
        ModelAndView mav = new ModelAndView("frame");
        List<Forward> forwards = null;
        List<Forward> sortedForwards = null;
        List<QualitySort> quality = null;
        Set<QualitySort> qualitySet = null;
        List<Integer> allIds = null;
        List<Integer> sortedIds = null;
        List<Integer> everyIds = new ArrayList<Integer>();
        List<Integer> sortedEveryIds = new ArrayList<Integer>();
        Long totalCount = -1L;
        Page page = null;

        if (type.equals("question")) {
            qualitySet = new HashSet<QualitySort>();
            sortedIds = new ArrayList<Integer>();
            allIds = genIds(type, questionStr, qualitySet);
            quality = new ArrayList<QualitySort>(qualitySet);
            Collections.sort(quality);
            for (QualitySort url : quality) {
                sortedIds.add(Integer.parseInt(url.getUrl()));
            }
            if (allIds.size() > 0) {
                totalCount = questionSearchService.getQPageCounts(allIds);
                //设置分页对象
                page = PageUtil.executePage(request, totalCount);
                for (long i = page.getBeginIndex(); i < (Math.min(page.getEndinIndex(), allIds.size())); i++) {
                    everyIds.add(allIds.get((int) i));
                    sortedEveryIds.add(sortedIds.get((int) i));
                }
//                System.out.println(allIds.size() + everyIds.toString());
                Cookie cookie = getCookie(request, "zscNav");
                //首次获取cookie
                if (cookie == null) {
                    cookie = new Cookie("zscNav", "0");
                    cookie.setMaxAge(60);
                    response.addCookie(cookie);
                }
                //重新查询重置cookie
                if (StringUtils.isEmpty(request.getParameter("redirect")) && StringUtils.isEmpty(request.getParameter("pageAction"))) {
                    cookie.setValue("0");
                    cookie.setMaxAge(60);
                    response.addCookie(cookie);
                }
                //判断标签页，第一次cookie为null
                if (cookie == null || cookie.getValue().equals("0")) {
                    forwards = questionSearchService.selectQByPage(everyIds);
//                    System.out.println("sss3 " + forwards.size());
                    mav.addObject("forwards", forwards);
                    mav.addObject("nav", "0");
                } else {
                    sortedForwards = questionSearchService.selectQByPage(sortedEveryIds);
                    mav.addObject("sortedForwards", sortedForwards);
                    mav.addObject("nav", "1");
                }
            }

        } else if (type.equals("people")) {
            allIds = genIds(type, questionStr, qualitySet);

            if (allIds.size() > 0) {
                totalCount = questionSearchService.getPPageCounts(allIds);
                //设置分页对象
                page = PageUtil.executePage(request, totalCount);
                for (long i = page.getBeginIndex(); i < (Math.min(page.getEndinIndex(), allIds.size())); i++) {
                    everyIds.add(allIds.get((int) i));
                }
                forwards = questionSearchService.selectPByPage(everyIds);
                mav.addObject("forwards", forwards);
            }

        } else {
            allIds = genIds(type, questionStr, qualitySet);
            if (allIds.size() > 0) {
                totalCount = questionSearchService.getTPageCounts(allIds);
                //设置分页对象
                page = PageUtil.executePage(request, totalCount);
                for (long i = page.getBeginIndex(); i < (Math.min(page.getEndinIndex(), allIds.size())); i++) {
                    everyIds.add(allIds.get((int) i));
                }
                forwards = questionSearchService.selectTByPage(everyIds);
                mav.addObject("forwards", forwards);
            }
        }
        mav.addObject("idCount", allIds.size());
        mav.addObject("type", type);
        mav.addObject("q", questionStr);
        return mav;
    }

    /**
     * setCookie 0
     *
     * @return
     * @RequestParam(value="q")中value要和jsp的input的name(只有name，id无所谓)相同，方法为get，post不显示
     */
    @RequestMapping("/nav0")
    public String nav0(HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes,@RequestParam(value = "q") String questionStr) {


//        Cookie[] cookies = request.getCookies();
//        if (cookies == null) {
//            System.out.println("没有cookie==============");
//        } else {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals("zscNav")) {
//                    System.out.println("原值为:" + cookie.getValue());
//                    cookie.setValue("0");
////                    cookie.setPath("/");
//                    cookie.setMaxAge(60 * 60);// 设置为60min
//                    response.addCookie(cookie);
//                    break;
//                }
//            }
//        }
        if (getCookie(request, "zscNav") == null) {
            Cookie cookie = new Cookie("zscNav", "0");
            cookie.setMaxAge(60);// 设置为60min
            response.addCookie(cookie);
        } else {
            Cookie cookie = getCookie(request, "zscNav");
            cookie.setValue("0");
            cookie.setMaxAge(60);// 设置为60min
            response.addCookie(cookie);
        }


        attributes.addAttribute("type", "question");
        attributes.addAttribute("q", questionStr);
        attributes.addAttribute("redirect", "0");

        return "redirect:/search";
    }

    @RequestMapping("/nav1")
    public String nav1(HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes,@RequestParam(value = "q") String questionStr) {
//        Cookie[] cookies = request.getCookies();
//        if (cookies == null) {
//            System.out.println("没有cookie==============");
//        } else {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals("zscNav")) {
//                    System.out.println("原值为:" + cookie.getValue());
//                    cookie.setValue("1");
////                    cookie.setPath("/");
//                    cookie.setMaxAge(60);// 设置为60min
//                    response.addCookie(cookie);
//                    break;
//                }
//            }
//        }
        if (getCookie(request, "zscNav") == null) {
            Cookie cookie = new Cookie("zscNav", "1");
            cookie.setMaxAge(60);
            response.addCookie(cookie);
        } else {
            Cookie cookie = getCookie(request, "zscNav");
            cookie.setValue("1");
            cookie.setMaxAge(60);
            response.addCookie(cookie);
        }
        attributes.addAttribute("type", "question");
        attributes.addAttribute("q", questionStr);
        attributes.addAttribute("redirect", "1");
        return "redirect:/search";
    }


    /**
     * 根据Cookie名获取对应的Cookie
     *
     * @param request HttpServletRequest
     * @param cookieName cookie名称
     *
     * @return 对应cookie，如果不存在则返回null
     */
    public static Cookie getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null || cookieName == null || cookieName.equals(""))
            return null;

        for (Cookie c : cookies) {
            if (c.getName().equals(cookieName)){
                return c;
            }
        }
        return null;
    }


    private List<Integer> genIds(String type, String str, Set<QualitySort> qualitySet) {
        System.out.println(str);
        //得到按序排列的关键字集合
        List<Term> terms = Filter.accept(StandardTokenizer.segment(str));
        System.out.println("分词结果 " + terms.toString());

        List<Integer> ids = new ArrayList<Integer>();
        List<Reverse> keyWords = new ArrayList<Reverse>();
        Reverse key;
        for (Term term : terms) {
            if (type.equals("question")) {
                key = questionSearchService.getQUrls(term.word);
            } else if (type.equals("people")) {
                key = questionSearchService.getPUrls(term.word);
            } else {
                key = questionSearchService.getTUrls(term.word);
            }


            if (key != null) {
                keyWords.add(key);
            }
        }

        //此处有区别，使用type区分(对people和topic进行分词，全部统一)@Deprecated
//        if (type.equals("question")) {
//            for (Term term : terms) {
//                if (rQuestionsMap.containsKey(term.word)) {
//                    keyWords.add(rQuestionsMap.get(term.word));
//                }
//            }
//        } else {
//            for (Term term : terms) {
//                for (Map.Entry<String, Reverse> entry : rQuestionsMap.entrySet()) {
//                    if (entry.getKey().contains(term.word)) {
//                        keyWords.add(entry.getValue());
//                    }
//                }
//            }
//        }

        Collections.sort(keyWords);//按IDF大小排序

        //得到按序排列的url集合，只要string
        List<String> sortedUrls = new ArrayList<String>();
        for (Reverse reverse : keyWords) {
            sortedUrls.add(reverse.getUrls());
        }

        //得到最终排序
        Set<String> urls = new LinkedHashSet<String>();//保证按顺序且不重复
//        Set<QualitySort> quality = new TreeSet<QualitySort>();
        List<String> temp = new ArrayList<String>();
        int len = sortedUrls.size() + 1;
        if (type.equals("question")) {
            for (int i = len - 1; i != 0; i--) {
                genQInSequence(urls, qualitySet, sortedUrls, 0, i, temp);
            }
        } else {
            for (int i = len - 1; i != 0; i--) {
                genInSequence(urls, sortedUrls, 0, i, temp);
            }
        }

        //显示结果
//        System.out.println("最终url: " + urls);
        for (String url : urls) {
            ids.add(Integer.parseInt(url));
        }

        return ids;
    }

    //返回List<Forward>，@Deprecated
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
//        System.out.println("最终url: " + urls);
        for (String num : urls) {
            if (fQuestionsMap.containsKey(num)) {
                forwards.add(fQuestionsMap.get(num));
            }
        }
//        for (int i = 0; i < forwards.size(); i++) {
//            System.out.println("最终结果  " + forwards.get(i).getId());
//        }
        return forwards;
    }

    //非问题递归获取全组合，并返回求交集后的结果
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

    //问题递归获取全组合，并返回求交集后的结果
    private void genQInSequence(Set<String> urls, Set<QualitySort> qualitySet, List<String> sortedUrls, int start, int len, List<String> temp) {//len为组合的长度
        if (len == 0) {
            List<TF_IDF> result = new ArrayList<TF_IDF>();
            for (int i = 0; i < Arrays.asList(temp.get(0).split(Config.DELIMITER)).size(); i++) {
                TF_IDF tf_idf = new TF_IDF(Arrays.asList(temp.get(0).split(Config.DELIMITER)).get(i));
                result.add(tf_idf);
            }
            for (int i = 1; i < temp.size(); i++) {
                List<TF_IDF> result2 = new ArrayList<TF_IDF>();//后面的存到一个数组里，与result求交
                for (int j = 0; j < Arrays.asList(temp.get(i).split(Config.DELIMITER)).size(); j++) {
                    TF_IDF tf_idf = new TF_IDF(Arrays.asList(temp.get(i).split(Config.DELIMITER)).get(j));
                    result2.add(tf_idf);
                }
                result.retainAll(result2);
            }
            Collections.sort(result);
//            System.out.println(result.toString());
            for (int i = 0; i < result.size(); i++) {
                urls.add(result.get(i).getUrl());
                qualitySet.add(new QualitySort(result.get(i).getUrl(), result.get(i).getQuality()));
            }
//            urls.addAll(result);
            return;
        }
        if (start == sortedUrls.size()) {
            return;
        }
        temp.add(sortedUrls.get(start));
        genQInSequence(urls, qualitySet, sortedUrls, start + 1, len - 1, temp);
        temp.remove(temp.size() - 1);
        genQInSequence(urls, qualitySet, sortedUrls, start + 1, len, temp);
    }

    class TF_IDF implements Comparable<TF_IDF> {
        private double TF;
        private double IDF;
        private String url;
        private int quality;

        public String getUrl() {
            return url;
        }

        public int getQuality() {
            return quality;
        }

        TF_IDF(String str) {
            this.url = str.split(",")[0];
            this.TF = Double.parseDouble(str.split(",")[1]);
            this.IDF = Double.parseDouble(str.split(",")[2]);
            this.quality = Integer.valueOf(str.split(",")[3]);
        }

        @Override
        public String toString() {
            return url.toString();
        }

        @Override
        public boolean equals(Object obj) {
            return url.equals(((TF_IDF) obj).getUrl());
        }

        @Override
        public int hashCode() {
            return url.hashCode();
        }

        public boolean equal(double a, double b) {
            if ((a - b > -0.000001) && (a - b) < 0.000001)
                return true;
            else
                return false;
        }

        //升序！！！
        @Override
        public int compareTo(TF_IDF object) {
            double str1 = TF * IDF;
            double str2 = object.TF * object.IDF;
            if (equal(str1, str2)) {
                if (quality < object.quality) {
                    return 1;
                } else {
                    return -1;
                }
            } else {
                if (str1 < str2) {
                    return 1;
                } else {
                    return -1;
                }
            }
        }
    }

    class QualitySort implements Comparable<QualitySort> {
        private String url;
        private int quality;

        public String getUrl() {
            return url;
        }

        public int getQuality() {
            return quality;
        }

        QualitySort(String url, int quality) {
            this.url = url;
            this.quality = quality;
        }

        @Override
        public String toString() {
            return url.toString();
        }

        @Override
        public boolean equals(Object obj) {
            return url.equals(((QualitySort) obj).getUrl());
        }

        @Override
        public int hashCode() {
            return url.hashCode();
        }

        //升序！！！
        @Override
        public int compareTo(QualitySort object) {
            if (quality < object.quality) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
