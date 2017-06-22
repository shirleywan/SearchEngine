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
    <link href="<c:url value='/static/css/bootstrap.min.css'/>" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="<c:url value='/static/js/jquery-3.2.1.min.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/static/js/bootstrap.min.js'/>"></script>
</head>
<body>
<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>
<form action="search" method="get">
    <div class="col-lg-6 col-md-offset-3">
        <div class="input-group input-group-lg">
            <input type="hidden" name="type" value="question">
            <input class="form-control" type="text" name="q" placeholder="搜索你感兴趣的内容...">
            <span class="input-group-btn"><button class="btn btn-primary" type="submit">Gooooo!</button></span>

            <%--<span class="input-group-addon btn btn-primary">Gooooo!</span>--%>
        </div>
    </div>
</form>

</body>
</html>
