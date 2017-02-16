<%--
  Created by IntelliJ IDEA.
  User: zsc
  Date: 2017/2/15
  Time: 16:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="page" value="${sessionScope.page}"/>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<c:set var="url" value="${param.url}"/>
<c:set var="urlParams" value="${param.urlParams}"/>
<c:set var="pathurl" value="${path}/${url}"/>
<tr>
    <td colspan="5">
        ${urlParams}
        共${page.totalCount}条记录 共${page.totalPage}页 每页显示${page.everyPage}条
        当前第${page.currentPage}页 
        <c:choose>
            <c:when test="${page.hasPrePage eq false}">
                <<首页 <上一页 
            </c:when>
            <c:otherwise>
                <a href="${pathurl}&pageAction=gopage&pageKey=1${urlParams}"><<首页</a> 
                <a href="${pathurl}&pageAction=gopage&pageKey=${((page.currentPage - 1) > 1)?(page.currentPage - 1) : 1}${urlParams}"/><上一页</a>
            </c:otherwise>
        </c:choose>
         || 
        <c:choose>
            <c:when test="${page.hasNextPage eq false}">
                 下一页> 末页>>
            </c:when>
            <c:otherwise>
                <a href="${pathurl}&pageAction=gopage&pageKey=${((page.currentPage + 1) < page.totalCount)?(page.currentPage+1) : page.totalCount}${urlParams}">下一页> </a> 
                <a href="${pathurl}&&pageAction=gopage&pageKey=${page.totalCount}${urlParams}">末页>></a>
            </c:otherwise>
        </c:choose>
         
        <SELECT name="indexChange" id="indexChange"
                onchange="getCurrentPage(this.value);">
            <c:forEach var="index" begin="1" end="${page.totalPage}" step="1">
                <option value="${index}" ${page.currentPage eq index ? "selected" : ""}>
                    第${index}页
                </option>
            </c:forEach>
        </SELECT>
         
        每页显示:<select name="everyPage" id="everyPage" onchange="setEveryPage(this.value);">
        <c:forEach var="pageCount" begin="1" end="${page.totalCount}" step="1">
            <option value="${pageCount}" ${page.everyPage eq pageCount ? "selected" : ""}>
                    ${pageCount}条
            </option>
        </c:forEach>
    </select>
    </td>
</tr>
<div style='display: none'>
    <a class=listlink id="indexPageHref" href='#'></a>
</div>
<script>
    function getCurrentPage(index) {
        var a = document.getElementById("indexPageHref");
        a.href = '${pathurl}&pageAction=gopage&pageKey=' + index + '${urlParams}';
        a.setAttribute("onclick", '');
        a.click("return false");
    }
    function setEveryPage(everyPage) {
        var a = document.getElementById("indexPageHref");
        var currentPage = document.getElementById('indexChange').value;
        a.href = '${pathurl}&pageAction=setpage&pageKey=' + everyPage + '${urlParams}';
        a.setAttribute("onclick", '');
        a.click("return false");
    }
    function sortPage(sortName) {
        var a = document.getElementById("indexPageHref");
        a.href = '${pathurl}&pageAction=sort&pageKey=' + sortName + '${urlParams}';
        a.setAttribute("onclick", '');
        a.click("return false");
    }
</script>
