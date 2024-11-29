<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="fr.cyu.jee.model.Subject" %>
<%@ page import="fr.cyu.jee.model.Teacher" %>
<%@ page import="fr.cyu.jee.model.User" %>
<%@ page import="fr.cyu.jee.model.UserType" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Modify a user</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css" />
    <script src="${pageContext.request.contextPath}/js/admin_add_users.js"></script>
</head>
<body class="main_body">
<% String title = "Modify a user"; %>
<%@ include file="banner.jsp" %>
<div class="centerdiv">
    <form name="modifyForm" method="post" action="${pageContext.request.contextPath}/admin/users">
        <input type="hidden" name="method" value="put"/>
        <input type="hidden" name="user" value="${user.id}"/>

        <label for="firstName">First name : </label>
        <input class="inputarea" type="text" id="firstName" name="firstName" value="${user.firstName}"/> <br/>
        <br>
        <label for="lastName">Last name : </label>
        <input class="inputarea" type="text" id="lastName" name="lastName" value="${user.lastName}"/> <br/>
        <br>
        <label for="dob">Date of birth : </label>
        <input class="inputarea" type="date" id="dob" name="dob" value="${user.dob}"/> <br/>
        <br>
        <label for="email">Email : </label>
        <input class="inputarea" type="email" id="email" name="email" value="${user.email}"/> <br/>
        <br>
        <label for="password">Password : </label>
        <input class="inputarea" type="password" id="password" name="password" value="${user.password}"/> <br/>
        <br>
        <%
            User usr = (User) request.getAttribute("user");
            if(usr.getUserType() == UserType.TEACHER) {
        %>
        <label for="subject">Subject:</label>
        <select id="subject" name="subject">
            <%
                for(Subject subject : (List<Subject>) request.getAttribute("subjects")) {
                    if(((Teacher) usr).getSubject().getId() == subject.getId()) {
            %>
            <option value="<%= subject.getId() %>" selected><%= subject.getName() %></option>
            <% } else { %>
            <option value="<%= subject.getId() %>"><%= subject.getName() %></option>
            <% }} %>
        </select>
        <% } %>
        <br>
        <br>
        <input type="submit" value="Submit"/>
    </form>





</div>
</div>


</body>
</html>
