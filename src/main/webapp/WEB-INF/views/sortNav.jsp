<%--
  Created by IntelliJ IDEA.
  User: zsc
  Date: 2017/6/21
  Time: 10:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="row">
    <div class="col-md-10">
        <ul class="nav nav-pills">
            <c:choose>
                <c:when test="${nav == '0'}">
                    <li role="presentation" class="active">
                        <a href="<%=request.getContextPath()%>/nav0">按相关度排序</a>
                    </li>
                    <li role="presentation">
                        <a href="<%=request.getContextPath()%>/nav1">按关注度排序</a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li role="presentation">
                        <a href="<%=request.getContextPath()%>/nav0">按相关度排序</a>
                    </li>
                    <li role="presentation" class="active">
                        <a href="<%=request.getContextPath()%>/nav1">按关注度排序</a>
                    </li>
                </c:otherwise>
            </c:choose>

        </ul>
    </div>
</div>

<div class="row">
    <div class="col-md-10">
        <hr>
    </div>
</div>
