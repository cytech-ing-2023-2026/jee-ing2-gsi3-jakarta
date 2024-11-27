<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Admin panel</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css" />
</head>
<body class="main_body">
<% String title = "Admin panel"; %>
<%@ include file="banner.jsp" %>
<div class="centerdiv">
  <div class="buttonDivContainer">
    <div class="buttonDiv" id="admin_users_menu" onclick="window.location.href='${pageContext.request.contextPath}/admin/users'">
      <p>Users</p>
    </div>
    <div class="buttonDiv" id="admin_planning" onclick="window.location.href='${pageContext.request.contextPath}/course'">
      <p>Courses</p>
    </div>
    <div class="buttonDiv" id="admin_grades" onclick="window.location.href='${pageContext.request.contextPath}/grades'">
      <p>Grades</p>
    </div>


  </div>
</div>


</body>
</html>

