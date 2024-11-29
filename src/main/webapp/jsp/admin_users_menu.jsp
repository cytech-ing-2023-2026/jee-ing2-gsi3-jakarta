<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Users</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css" />
</head>
<body class="main_body">
<% String title = "Users"; %>
<%@ include file="banner.jsp" %>
<div class="centerdiv">
    <div class="buttonDivContainer">
        <div class="buttonDiv" id="addUser" onclick="window.location.href='${pageContext.request.contextPath}/admin/users?selectedMenu=ADD_MENU'">
            <p>Add a user</p>
        </div>
        <div class="buttonDiv" id="displayUser" onclick="window.location.href='${pageContext.request.contextPath}/admin/users?selectedMenu=DISPLAY_MENU'">
            <p>List of users</p>
        </div>

    </div>
</div>


</body>
</html>