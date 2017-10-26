package com.szl.util;

import com.szl.Config;
import com.szl.domain.Reverse;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by zsc on 2017/7/19.
 * 生成页码
 */
public class PageNumUtil {
    public static List<List<Integer>> genIds(String type, List<Reverse> keyWords, List<List<Integer>> list) {
        List<Integer> defaultID = new ArrayList<Integer>();
        List<Integer> TFIDFID = new ArrayList<Integer>();
        List<Integer> qualityID = new ArrayList<Integer>();


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
        List<String> TFIDFUrls = new ArrayList<String>();
        List<String> defaultUrls = new ArrayList<String>();
        List<String> qualityUrls = new ArrayList<String>();

        for (Reverse reverse : keyWords) {
            TFIDFUrls.add(reverse.getTFIDF());
            defaultUrls.add(reverse.getPageID());
            qualityUrls.add(reverse.getQualityAndPID());
        }

        //得到最终排序
        Set<String> defaultSet = new LinkedHashSet<String>();//保证按顺序且不重复，按关联度&关注度
        Set<String> TFIDFSet = new LinkedHashSet<String>();//按TFIDF&关注度
        Set<String> qualitySet = new HashSet<String>();//按关注度


        List<String> TFIDFTemp = new ArrayList<String>();
        List<String> defaultTemp = new ArrayList<String>();
        List<String> qualityTemp = new ArrayList<String>();
        int len = defaultUrls.size();
        if (type.equals("question")) {
            for (int i = len; i != 0; i--) {
//                genQInSequence(defaultSet, TFIDFSet, qualitySet, defaultUrls, TFIDFUrls, qualityUrls, 0, i, TFIDFTemp, defaultTemp, qualityTemp);
                genQInSequence(defaultSet, TFIDFSet, qualitySet, defaultUrls, TFIDFUrls, qualityUrls, len, i);//非递归
            }
//            genQuestionList(defaultSet, TFIDFSet, qualitySet, defaultUrls, TFIDFUrls, qualityUrls, len);//相关性↑
        } else {
            for (int i = len; i != 0; i--) {
//                genInSequence(defaultSet, defaultUrls, 0, i, defaultTemp);//递归
                genInSequence(defaultSet, defaultUrls, len, i);//非递归

            }
        }

        //显示结果
        //默认
        for (String url : defaultSet) {
            defaultID.add(Integer.parseInt(url));
        }

        //TFIDF
        for (String url : TFIDFSet) {
            TFIDFID.add(Integer.parseInt(url));
        }

        //按关注度
        List<String> quality = new ArrayList<String>(qualitySet);

        Collections.sort(quality, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return -o1.compareTo(o2);
            }
        });
        for (String url : quality) {
            qualityID.add(Integer.parseInt(url.split(",")[1]));
        }

        if (type.equals("question")) {
            list.add(TFIDFID);
            list.add(defaultID);
        } else {
            list.add(defaultID);
        }
        return list;
    }

    //返回List<Forward>，@Deprecated
    /*private List<Forward> genUrls(String type, String str, Map<String, Forward> fQuestionsMap, Map<String, Reverse> rQuestionsMap) {
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
            sortedUrls.add(reverse.getPageID());
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
    }*/

    /**
     * 只取1234,123,12,1
     * @param defaultSet
     * @param TFIDFSet
     * @param qualitySet
     * @param defaultUrls
     * @param TFIDFUrls
     * @param qualityUrls
     * @param len
     */
    private static void genQuestionList(Set<String> defaultSet, Set<String> TFIDFSet, Set<String> qualitySet,
                                 List<String> defaultUrls, List<String> TFIDFUrls, List<String> qualityUrls, int len) {
        List<List<String>> defaultList = new ArrayList<List<String>>();
        List<List<String>> qualityList = new ArrayList<List<String>>();
        for (int i = 1; i <= len; i++) {
            if (defaultList.size() == 0) {
                for (int j = 0; j < i; j++) {
                    List<String> defaultResult = new ArrayList<String>();
                    List<String> qualityResult = new ArrayList<String>();

                    defaultResult.addAll(Arrays.asList(defaultUrls.get(0).split(Pattern.quote(Config.DELIMITER))));
                    qualityResult.addAll(Arrays.asList(qualityUrls.get(0).split(Pattern.quote(Config.DELIMITER))));
                    for (int k = 1; k < i; k++) {
                        defaultResult.retainAll(Arrays.asList(defaultUrls.get(k).split(Pattern.quote(Config.DELIMITER))));
                        qualityResult.retainAll(Arrays.asList(qualityUrls.get(k).split(Pattern.quote(Config.DELIMITER))));
                    }
                    defaultList.add(defaultResult);
                    qualityList.add(qualityResult);
                }
            } else {
                List<String> defaultResult = new ArrayList<String>(defaultList.get(defaultList.size() - 1));
                defaultResult.retainAll(Arrays.asList(defaultUrls.get(i - 1).split(Pattern.quote(Config.DELIMITER))));
                defaultList.add(defaultResult);
                List<String> qualityResult = new ArrayList<String>(qualityList.get(qualityList.size() - 1));
                qualityResult.retainAll(Arrays.asList(qualityUrls.get(i - 1).split(Pattern.quote(Config.DELIMITER))));
                qualityList.add(qualityResult);
            }
        }
        for (int i = defaultList.size() - 1; i >= 0; i--) {
            defaultSet.addAll(defaultList.get(i));
            qualitySet.addAll(qualityList.get(i));
        }

    }


    /**
     * 非递归begin
     */
    //获取长度为len的组合数C(arrLen,len)的个数
    private static int getCountOfCombinations(int arrLen, int len) {
        int m = 1;
        for (int i = 0; i < len; i++) {
            m *= arrLen - i;
        }
        int n = 1;
        for (int i = len; i > 1; i--) {
            n *= i;
        }
        return m / n;
    }


    private static void genInSequence(Set<String> defaultSet, List<String> defaultUrls, int len, int subLen) {
        List<String> tempList = new ArrayList<String>();
        int[] array = new int[len];
        for (int j = 0; j < len; j++) {
            if (j < subLen) {
                array[j] = 1;
            } else {
                array[j] = 0;
            }
        }


        //得到C(len,subLen)
        for (int j = 0; j < array.length; j++) {
            if (array[j] == 1) {
                tempList.add(defaultUrls.get(j));
            }
        }
        List<String> result = new ArrayList<String>();
        result.addAll(Arrays.asList(tempList.get(0).split(Pattern.quote(Config.DELIMITER))));
        for (int i = 1; i < tempList.size(); i++) {
            result.retainAll(Arrays.asList(tempList.get(i).split(Pattern.quote(Config.DELIMITER))));
        }
        defaultSet.addAll(result);



        int n = getCountOfCombinations(len, subLen);//得到C(len,i)组合数个数
        for (int j = 1; j < n; j++) {
            for (int k = array.length - 1; k > 0; k--) {
                if (array[k] == 0 && array[k - 1] == 1) {
                    array[k] = 1;
                    array[k - 1] = 0;
                    int start = k;
                    int end = len - 1;
                    while (true) {
                        while (array[start] == 1) {
                            start++;
                            if (start >= len)
                                break;
                        }
                        while (array[end] == 0) {
                            end--;
                            if (end < k)
                                break;
                        }

                        if (start < end) {
                            int temp = array[end];
                            array[end] = array[start];
                            array[start] = temp;
                        } else {
                            break;
                        }
                    }
                    break;
                }
            }
            //得到一条组合
            tempList = new ArrayList<String>();
            for (int k = 0; k < array.length; k++) {
                if (array[k] == 1) {
                    tempList.add(defaultUrls.get(k));
                }
            }
            result = new ArrayList<String>();
            result.addAll(Arrays.asList(tempList.get(0).split(Pattern.quote(Config.DELIMITER))));
            for (int i = 1; i < tempList.size(); i++) {
                result.retainAll(Arrays.asList(tempList.get(i).split(Pattern.quote(Config.DELIMITER))));
            }
            defaultSet.addAll(result);
        }
    }


    /**
     * 本身ID就是按照quality排序的，所以直接调用retainAll即可
     * @param defaultSet
     * @param TFIDFSet
     * @param qualitySet
     * @param defaultUrls
     * @param TFIDFUrls
     * @param qualityUrls
     * @param len
     * @param subLen
     */
    private static void genQInSequence(Set<String> defaultSet, Set<String> TFIDFSet, Set<String> qualitySet, List<String> defaultUrls,
                                List<String> TFIDFUrls, List<String> qualityUrls, int len, int subLen) {
        List<String> defaultTemp = new ArrayList<String>();
        List<String> TFIDFTemp = new ArrayList<String>();
        List<String> qualityTemp = new ArrayList<String>();

        int[] array = new int[len];
        for (int j = 0; j < len; j++) {
            if (j < subLen) {
                array[j] = 1;
            } else {
                array[j] = 0;
            }
        }

        //得到C(len,subLen)
        for (int j = 0; j < array.length; j++) {
            if (array[j] == 1) {
                defaultTemp.add(defaultUrls.get(j));
                TFIDFTemp.add(TFIDFUrls.get(j));
                qualityTemp.add(qualityUrls.get(j));
            }
        }
        List<String> TFIDFResult = new ArrayList<String>();
        List<String> tempList = new ArrayList<String>();
        List<String> defaultResult = new ArrayList<String>();
        List<String> qualityResult = new ArrayList<String>();
        defaultResult.addAll(Arrays.asList(defaultTemp.get(0).split(Pattern.quote(Config.DELIMITER))));
        qualityResult.addAll(Arrays.asList(qualityTemp.get(0).split(Pattern.quote(Config.DELIMITER))));

        HashMap<String, String> resultMap = new HashMap<String, String>();
        for (String str : Arrays.asList(TFIDFTemp.get(0).split(Pattern.quote(Config.DELIMITER)))) {
            resultMap.put(str.split(",")[0], str.split(",")[1]);
        }
        if (defaultTemp.size() == 1) {
//                ArrayList<String> list = new ArrayList<String>();
            for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                tempList.add(new StringBuilder().append(entry.getValue()).append(",").append(entry.getKey()).toString());
            }
            Collections.sort(tempList, new Comparator<String>() {
                @Override
                public int compare(String str1, String str2) {
                    return -str1.compareTo(str2);
                }
            });
        }
        for (int i = 1; i < defaultTemp.size(); i++) {
            HashMap<String, String> resultMap2 = new HashMap<String, String>();
            for (String str : Arrays.asList(TFIDFTemp.get(i).split(Pattern.quote(Config.DELIMITER)))) {
                resultMap2.put(str.split(",")[0], str.split(",")[1]);
            }
            resultMap = intersect(resultMap, resultMap2);
            defaultResult.retainAll(Arrays.asList(defaultTemp.get(i).split(Pattern.quote(Config.DELIMITER))));
            qualityResult.retainAll(Arrays.asList(qualityTemp.get(i).split(Pattern.quote(Config.DELIMITER))));
        }

        for (Map.Entry<String, String> entry : resultMap.entrySet()) {
            //TFIDF的和，PID
            tempList.add(new StringBuilder().append(entry.getValue()).append(",").append(entry.getKey()).toString());
        }
        Collections.sort(tempList, new Comparator<String>() {
            @Override
            public int compare(String str1, String str2) {
                return -str1.compareTo(str2);
            }
        });
        for (String str : tempList) {
            TFIDFResult.add(str.split(",")[1]);
        }
        TFIDFSet.addAll(TFIDFResult);
        defaultSet.addAll(defaultResult);
        qualitySet.addAll(qualityResult);



        int n = getCountOfCombinations(len, subLen);//得到C(len,i)组合数个数
        for (int j = 1; j < n; j++) {
            for (int k = array.length - 1; k > 0; k--) {
                if (array[k] == 0 && array[k - 1] == 1) {
                    array[k] = 1;
                    array[k - 1] = 0;
                    int start = k;
                    int end = len - 1;
                    while (true) {
                        while (array[start] == 1) {
                            start++;
                            if (start >= len)
                                break;
                        }
                        while (array[end] == 0) {
                            end--;
                            if (end < k)
                                break;
                        }

                        if (start < end) {
                            int temp = array[end];
                            array[end] = array[start];
                            array[start] = temp;
                        } else {
                            break;
                        }
                    }
                    break;
                }
            }


            //得到一条组合
            defaultTemp = new ArrayList<String>();
            TFIDFTemp = new ArrayList<String>();
            qualityTemp = new ArrayList<String>();
            for (int k = 0; k < array.length; k++) {
                if (array[k] == 1) {
                    defaultTemp.add(defaultUrls.get(k));
                    TFIDFTemp.add(TFIDFUrls.get(k));
                    qualityTemp.add(qualityUrls.get(k));
                }
            }
            TFIDFResult = new ArrayList<String>();
            tempList = new ArrayList<String>();
            defaultResult = new ArrayList<String>();
            qualityResult = new ArrayList<String>();
            defaultResult.addAll(Arrays.asList(defaultTemp.get(0).split(Pattern.quote(Config.DELIMITER))));
            qualityResult.addAll(Arrays.asList(qualityTemp.get(0).split(Pattern.quote(Config.DELIMITER))));

            resultMap = new HashMap<String, String>();
            for (String str : Arrays.asList(TFIDFTemp.get(0).split(Pattern.quote(Config.DELIMITER)))) {
                resultMap.put(str.split(",")[0], str.split(",")[1]);
            }
            if (defaultTemp.size() == 1) {
                for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                    tempList.add(new StringBuilder().append(entry.getValue()).append(",").append(entry.getKey()).toString());
                }
                Collections.sort(tempList, new Comparator<String>() {
                    @Override
                    public int compare(String str1, String str2) {
                        return -str1.compareTo(str2);
                    }
                });
            }
            for (int i = 1; i < defaultTemp.size(); i++) {
                HashMap<String, String> resultMap2 = new HashMap<String, String>();
                for (String str : Arrays.asList(TFIDFTemp.get(i).split(Pattern.quote(Config.DELIMITER)))) {
                    resultMap2.put(str.split(",")[0], str.split(",")[1]);
                }
                resultMap = intersect(resultMap, resultMap2);
                defaultResult.retainAll(Arrays.asList(defaultTemp.get(i).split(Pattern.quote(Config.DELIMITER))));
                qualityResult.retainAll(Arrays.asList(qualityTemp.get(i).split(Pattern.quote(Config.DELIMITER))));
            }

            for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                //TFIDF的和，PID
                tempList.add(new StringBuilder().append(entry.getValue()).append(",").append(entry.getKey()).toString());
            }
            Collections.sort(tempList, new Comparator<String>() {
                @Override
                public int compare(String str1, String str2) {
                    return -str1.compareTo(str2);
                }
            });
            for (String str : tempList) {
                TFIDFResult.add(str.split(",")[1]);
            }
            TFIDFSet.addAll(TFIDFResult);
            defaultSet.addAll(defaultResult);
            qualitySet.addAll(qualityResult);
        }
    }
    /**
     * 非递归end
     */


    /**
     * 递归begin
     */

    //非问题递归获取全组合，并返回求交集后的结果
    private static void genInSequence(Set<String> defaultSet, List<String> defaultUrls, int start, int len, List<String> temp) {//len为组合的长度
        if (len == 0) {
            List<String> result = new ArrayList<String>();
            result.addAll(Arrays.asList(temp.get(0).split(Pattern.quote(Config.DELIMITER))));
            for (int i = 1; i < temp.size(); i++) {
                result.retainAll(Arrays.asList(temp.get(i).split(Pattern.quote(Config.DELIMITER))));
            }
            defaultSet.addAll(result);
            return;
        }
        if (start == defaultUrls.size()) {
            return;
        }
        temp.add(defaultUrls.get(start));
        genInSequence(defaultSet, defaultUrls, start + 1, len - 1, temp);
        temp.remove(temp.size() - 1);
        genInSequence(defaultSet, defaultUrls, start + 1, len, temp);
    }

    //问题递归获取全组合，并返回求交集后的结果
    private static void genQInSequence(Set<String> defaultSet, Set<String> TFIDFSet, Set<String> qualitySet, List<String> defaultUrls,
                                List<String> TFIDFUrls, List<String> qualityUrls, int start, int len,
                                List<String> TFIDFTemp, List<String> defaultTemp, List<String> qualityTemp) {//len为组合的长度
        if (len == 0) {
            List<String> TFIDFResult = new ArrayList<String>();
            List<String> tempList = new ArrayList<String>();
            List<String> defaultResult = new ArrayList<String>();
            List<String> qualityResult = new ArrayList<String>();
            defaultResult.addAll(Arrays.asList(defaultTemp.get(0).split(Pattern.quote(Config.DELIMITER))));
            qualityResult.addAll(Arrays.asList(qualityTemp.get(0).split(Pattern.quote(Config.DELIMITER))));

            HashMap<String, String> resultMap = new HashMap<String, String>();
            for (String str : Arrays.asList(TFIDFTemp.get(0).split(Pattern.quote(Config.DELIMITER)))) {
                resultMap.put(str.split(",")[0], str.split(",")[1]);
            }
            if (defaultTemp.size() == 1) {
                for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                    tempList.add(new StringBuilder().append(entry.getValue()).append(",").append(entry.getKey()).toString());
                }
                Collections.sort(tempList, new Comparator<String>() {
                    @Override
                    public int compare(String str1, String str2) {
                        return -str1.compareTo(str2);
                    }
                });
            }
            for (int i = 1; i < defaultTemp.size(); i++) {
                HashMap<String, String> resultMap2 = new HashMap<String, String>();
                for (String str : Arrays.asList(TFIDFTemp.get(i).split(Pattern.quote(Config.DELIMITER)))) {
                    resultMap2.put(str.split(",")[0], str.split(",")[1]);
                }
                resultMap = intersect(resultMap, resultMap2);
                defaultResult.retainAll(Arrays.asList(defaultTemp.get(i).split(Pattern.quote(Config.DELIMITER))));
                qualityResult.retainAll(Arrays.asList(qualityTemp.get(i).split(Pattern.quote(Config.DELIMITER))));
            }

            for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                //TFIDF的和，PID
                tempList.add(new StringBuilder().append(entry.getValue()).append(",").append(entry.getKey()).toString());
            }
            Collections.sort(tempList, new Comparator<String>() {
                @Override
                public int compare(String str1, String str2) {
                    return -str1.compareTo(str2);
                }
            });
            for (String str : tempList) {
                TFIDFResult.add(str.split(",")[1]);
            }
            TFIDFSet.addAll(TFIDFResult);
            defaultSet.addAll(defaultResult);
            qualitySet.addAll(qualityResult);
            return;
        }
        if (start == defaultUrls.size()) {
            return;
        }
        TFIDFTemp.add(TFIDFUrls.get(start));
        defaultTemp.add(defaultUrls.get(start));
        qualityTemp.add(qualityUrls.get(start));
        genQInSequence(defaultSet, TFIDFSet, qualitySet, defaultUrls,TFIDFUrls, qualityUrls, start + 1, len - 1, TFIDFTemp, defaultTemp, qualityTemp);
        TFIDFTemp.remove(TFIDFTemp.size() - 1);
        defaultTemp.remove(defaultTemp.size() - 1);
        qualityTemp.remove(qualityTemp.size() - 1);
        genQInSequence(defaultSet, TFIDFSet, qualitySet, defaultUrls, TFIDFUrls, qualityUrls, start + 1, len, TFIDFTemp, defaultTemp, qualityTemp);
    }

    private static HashMap<String, String> intersect(HashMap<String, String> hashMap1, HashMap<String, String> hashMap2) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        if (hashMap1.size() > hashMap2.size()) {
            for (Map.Entry<String, String> entry : hashMap1.entrySet()) {
                if (hashMap2.containsKey(entry.getKey())) {
                    //TFIDF的和，PID
                    hashMap.put(entry.getKey(), new StringBuilder().append(String.valueOf(Double.valueOf(entry.getValue()) +
                            Double.valueOf(hashMap2.get(entry.getKey())))).toString());
                }
            }
        } else {
            for (Map.Entry<String, String> entry : hashMap2.entrySet()) {
                if (hashMap1.containsKey(entry.getKey())) {
                    //TFIDF的和，PID
                    hashMap.put(entry.getKey(), new StringBuilder().append(String.valueOf(Double.valueOf(entry.getValue()) +
                            Double.valueOf(hashMap1.get(entry.getKey())))).toString());
                }
            }
        }
        return hashMap;
    }
    /**
     * 递归end
     */

    /*class TF_IDF implements Comparable<TF_IDF> {
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
    }*/

    /*class QualitySort implements Comparable<QualitySort> {
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
    }*/

}
