<%--
  Created by IntelliJ IDEA.
  User: zsc
  Date: 2017/1/18
  Time: 21:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Search</title>
</head>
<body>
<form action="search" method="get">
    <p>
        <input type="hidden" name="type" value="content">
        <input type="text" name="q">
    </p>

    <p>
        <button type="submit">提交</button>
    </p>
</form>

</body>
</html>
