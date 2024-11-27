<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Home</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css" />
</head>
<body class="main_body">
<% String title = "Welcome " + ((fr.cyu.jee.model.User)session.getAttribute("user")).getFirstName(); %>
<%@ include file="banner.jsp" %>
<div class="centerdiv">
    <c:if test='${pageContext.request.getAttribute("error") != null}'>
        <label class="error">${pageContext.request.getAttribute("error")}</label>
    </c:if>

    <div class="buttonDivContainer">
        <div class="buttonDiv" id="planning"  onclick="window.location.href='${pageContext.request.contextPath}/course'">
            <p>Courses</p>
        </div>
        <div class="buttonDiv" id="gradesStudent"  onclick="window.location.href='${pageContext.request.contextPath}/grades'">
            <p>Grades</p>
        </div>
    </div>
</div>


</body>
</html>
