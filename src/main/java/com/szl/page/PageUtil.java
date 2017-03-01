package com.szl.page;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zsc on 2017/2/15.
 */
public class PageUtil {
    public static final String ASC = "asc";
    public static final String DESC = "desc";
    public static final String PAGE_DESC = "↓";
    public static final String PAGE_ASC = "↑";
    public static final String PAGE_NULL = "  ";
    public static final String SESSION_PAGE_KEY = "page";


    /**
     * 初始化分页类
     *
     * @param totalCount 总行数
     * @param state      分页状态
     * @param value      只有在设置每页显示多少条时,值不会NULL,其它为NULL
     */
    public static Page inintPage(Long totalCount, Integer state, String value, Page sessionPage) {
        Page page = null;
        if (state < 0) {
            page = new Page(totalCount);
        } else {
            /**每页显示多少条*/
            Long everPage = null == value ? 1L : Long.parseLong(value);
            /**获取Session中的分页类,方便保存页面分页状态*/
            page = sessionPage;
            page.setEveryPage(everPage);
            page.setTotalCount(totalCount);//因为sessionPage是new Page()，没有设置totalCount，因此这里要设置
        }
        return page;
    }


    /**
     * 当页点击：首页,前一页,后一页,末页,排序,到第多少页时进行分页操作
     *
     * @param state 分页状态
     * @param value 排序字段名或者到第多少页
     */
    public static Page execPage(int state, String value, Page sessionPage) {
        Page page = sessionPage;
        /**调用方法进行分页计算*/
        page.pageState(state, value);
        return page;
    }


    //BaseController
    /**
     * oracel的三层分页语句 子类在展现数据前,进行分页计算!
     *
     * @param totalCount          根据查询SQL获取的总条数
     * @param columnNameDescOrAsc 列名+排序方式 : ID DESC or ASC
     */
    public static Page executePage(HttpServletRequest request, Long totalCount) {
        if (null == totalCount) {
            totalCount = 0L;
        }
        /** 页面状态,这个状态是分页自带的,与业务无关 */
        String pageAction = request.getParameter("pageAction");
        String value = request.getParameter("pageKey");

        /** 获取下标判断分页状态 */
        int state = PageState.getOrdinal(pageAction);

        Page page = null;
        /**
         * state < 1 只有二种状态
         * 1 当首次调用时,分页状态类中没有值为 NULL 返回 -1
         * 2 当页面设置每页显示多少条: index=0,当每页显示多少条时,分页类要重新计算
         * */
        Page sessionPage = getPage(request);

        if (state < 1) {
//            System.out.println("新建page......");
            page = inintPage(totalCount, state, value, sessionPage);
        } else {
//            System.out.println("session获取page......");
            page = execPage(state, value, sessionPage);
        }
        setSession(request, page);
        return page;
    }

    private static Page getPage(HttpServletRequest request) {
        Page page = (Page) request.getSession().getAttribute(SESSION_PAGE_KEY);
        if (page == null) {
            page = new Page();
        }
        return page;
    }

    public static void setSession(HttpServletRequest request, Page page) {
        request.getSession().setAttribute(SESSION_PAGE_KEY, page);
    }
}
