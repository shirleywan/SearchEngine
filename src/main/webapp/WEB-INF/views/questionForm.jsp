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
    </table>
</div>
</body>
</html>
