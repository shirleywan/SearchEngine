package com.szl.page;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zsc on 2017/2/15.
 */
public class BaseController {
    /**
     * oracel的三层分页语句 子类在展现数据前,进行分页计算!
     *
     * @param totalCount          根据查询SQL获取的总条数
     * @param columnNameDescOrAsc 列名+排序方式 : ID DESC or ASC
     */
    protected Page executePage(HttpServletRequest request, Long totalCount) {
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
         * index < 1 只有二种状态
         * 1 当首次调用时,分页状态类中没有值为 NULL 返回 -1
         * 2 当页面设置每页显示多少条: index=0,当每页显示多少条时,分页类要重新计算
         * */
        Page sessionPage = getPage(request);

        if (state < 1) {
            page = PageUtil.inintPage(totalCount, state, value, sessionPage);
        } else {
            page = PageUtil.execPage(state, value, sessionPage);
        }
        setSession(request, page);
        return page;
    }

    private Page getPage(HttpServletRequest request) {
        Page page = (Page) request.getSession().getAttribute(
                PageUtil.SESSION_PAGE_KEY);
        if (page == null) {
            page = new Page();
        }
        return page;
    }

    private void setSession(HttpServletRequest request, Page page) {
        request.getSession().setAttribute(PageUtil.SESSION_PAGE_KEY, page);
    }
}
