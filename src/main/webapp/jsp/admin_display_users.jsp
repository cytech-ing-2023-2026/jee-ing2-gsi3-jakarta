<%@ page import="fr.cyu.jee.model.Teacher" %>
<%@ page import="fr.cyu.jee.model.User" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>List of users</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css" />
</head>
<body class="main_body">
<% String title = "List of users"; %>
<%@ include file="banner.jsp" %>
<div class="centerdiv">
    <form name="searchForm" method="get" action="${pageContext.request.contextPath}/admin/users">
        <input type="hidden" name="selectedMenu" value="DISPLAY_MENU">

        <label for="emailSearch">Email : </label>
        <input class="inputarea" type="text" id="emailSearch" name="emailSearch"/>

        <label for="userType">User type:</label>
        <select name="userType" id="userType" onchange="updateUserType()">
            <option value=""></option>
            <option value="ADMIN">Admin</option>
            <option value="TEACHER">Teacher</option>
            <option value="STUDENT">Student</option>
        </select>

        <input type="submit" value="Submit"/>
        <br>
        <br>
    </form>

    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>User type</th>
            <th>First name</th>
            <th>Last name</th>
            <th>Email</th>
            <th>Date of Birth</th>
            <th>Subject</th>
            <th>Modify</th>
            <th>Remove</th>
        </tr>
        </thead>
        <tbody>
        <%
            List<User> users = (List<User>) request.getAttribute("users");
            for (User user : users) {
        %>
        <tr>
            <td><%= user.getId() %></td>
            <td><%= user.getUserType() %></td>
            <td><%= user.getFirstName() %></td>
            <td><%= user.getLastName() %></td>
            <td><%= user.getEmail() %></td>
            <td><%= user.getDob() %></td>
            <td>
                <%
                    if(user instanceof Teacher) {
                %>
                <%= ((Teacher) user).getSubject().getName() %>
                <% } else { %>
                -
                <% } %>
            </td>
            <td>
                <form action="${pageContext.request.contextPath}/admin/users" method="get" style="display:inline;">
                    <input type="hidden" name="selectedMenu" value="EDIT_MENU">
                    <input type="hidden" name="selectedUser" value="<%= user.getId() %>">
                    <button type="submit">Modify</button>
                </form>
            </td>
            <td>
                <form action="${pageContext.request.contextPath}/admin/users" method="post" style="display:inline;">
                    <input type="hidden" name="method" value="delete">
                    <input type="hidden" name="user" value="<%= user.getId() %>">
                    <button type="submit">Remove</button>
                </form>
            </td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>
</div>
</div>


</body>
</html>
