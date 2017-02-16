<%--
  Created by IntelliJ IDEA.
  User: zsc
  Date: 2017/1/18
  Time: 15:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Question</title>
    <style type="text/css">@import url(css/main.css);</style>
</head>
<body>
<div id="global">
    <ul>
        <li>
            <%--使用<%=request.getContextPath()%>得到工程名SearchEngine--%>
            <a href="<%=request.getContextPath()%>/search?type=question&q=${q}">内容</a>
        </li>
        <li>
            <a href="<%=request.getContextPath()%>/search?type=people&q=${q}">用户</a>
        </li>
        <li>
            <a href="<%=request.getContextPath()%>/search?type=topic&q=${q}">话题</a>
        </li>
    </ul>

    <h1>Question List</h1>
    <table>
        <tr>
            <th>ID</th>
            <th>Url</th>
            <th>Title</th>
            <th>&nbsp;</th>
        </tr>
        <c:forEach items="${forwards}" var="forward">
            <tr>
                <td>${forward.id}</td>
                <td><a href="${forward.url}"> ${forward.url}</a></td>
                <td><a href="${forward.url}"> ${forward.title}</a></td>
            </tr>
        </c:forEach>
        <jsp:include page="page.jsp">
            <jsp:param name="url" value="search?type=question&q=${q}" />
        </jsp:include>
    </table>
</div>
</body>
</html>
