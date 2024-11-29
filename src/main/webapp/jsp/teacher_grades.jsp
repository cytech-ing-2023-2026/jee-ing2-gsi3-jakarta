<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="fr.cyu.jee.model.Grade" %>
<%@ page import="java.util.List" %>
<%@ page import="fr.cyu.jee.model.User" %>
<%@ page import="fr.cyu.jee.model.UserType" %>
<%@ page import="fr.cyu.jee.model.Subject" %>
<%@ page import="fr.cyu.jee.util.CustomDateTimeFormatter" %>
<!DOCTYPE html>
<html>
<head>
    <title>Grades</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css" />
    <style>
        /* Alignement des champs du formulaire */
        .form-group {
            display: flex;
            align-items: center;
            margin-bottom: 10px;
        }
        .form-group label {
            flex: 0 0 150px; /* Largeur fixe pour aligner les étiquettes 0 0 150 est le juste milieu ! */
            text-align: right;
            margin-right: 10px;
        }
        .form-group input {
            flex: 1;
        }
        .grades-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        .grades-table th, .grades-table td {
            border: 1px solid black;
            padding: 8px;
            text-align: center;
        }
        .grades-table th {
            background-color: #f2f2f2;
        }
    </style>
</head>
<body class="main_body">
<% String title = "Grades"; %>
<%@ include file="banner.jsp" %>
<div class="centerdiv">
    <div style="display: flex;">
        <!-- Formulaire à gauche -->
        <div style="flex: 1; padding: 10px; border-right: 1px solid gray;">
            <form name="addGradesForm" method="post" action="${pageContext.request.contextPath}/grades">
                <div class="form-group">
                    <label for="email">Student Email:</label>
                    <input class="inputarea" type="email" id="email" name="email" required />
                </div>
                <div class="form-group">
                    <label for="grade">Grade:</label>
                    <input class="inputarea" type="number" id="grade" name="grade" step="0.01" min="0" required />
                </div>
                <%
                    if(((User)session.getAttribute("user")).getUserType() == UserType.ADMIN) {
                %>
                <div class="form-group">
                    <label for="subject_add">Subject:</label>
                    <select class="inputarea" id="subject_add" name="subject" required>
                        <%
                            for(Subject subject : (List<Subject>) pageContext.getRequest().getAttribute("subjects")) {
                        %>
                        <option value="<%= subject.getId() %>"><%= subject.getName() %></option>
                        <% } %>
                    </select>
                </div>
                <% } %>
                <input type="submit" value="Submit" />
            </form>
        </div>

        <!-- Liste des notes à droite -->
        <div style="flex: 2; padding: 10px;">
            <h2>Student Grades</h2>
            <table class="grades-table">
                <thead>
                <tr>
                    <th>Last Name</th>
                    <th>First Name</th>
                    <th>Subject</th>
                    <th>Grade</th>
                    <th>Created At</th>
                    <th>Updated At</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <%
                    for(Grade grade : (List<Grade>) pageContext.getRequest().getAttribute("grades")) {
                        String deleteForm = "delete_form_" + grade.getId();
                        String updateForm = "update_form_" + grade.getId();
                %>
                <tr>
                    <form id="<%= updateForm %>" action="${pageContext.request.contextPath}/grades" method="post"></form>
                    <form id="<%= deleteForm %>" action="${pageContext.request.contextPath}/grades" method="post"></form>

                    <input type="hidden" name="grade" value="<%= grade.getId() %>" form="<%= updateForm %>">
                    <input type="hidden" name="grade" value="<%= grade.getId() %>" form="<%= deleteForm %>">

                    <input type="hidden" name="method" value="put" form="<%= updateForm %>">
                    <input type="hidden" name="method" value="delete" form="<%= deleteForm %>">

                    <td><%= grade.getStudent().getLastName().toUpperCase() %></td>
                    <td><%= grade.getStudent().getFirstName() %></td>
                    <td>
                        <%
                            if(((User)session.getAttribute("user")).getUserType() == UserType.ADMIN) {
                        %>
                        <select class="inputarea" id="subject" name="subject" form="<%= updateForm %>" required>
                            <%
                                for(Subject subject : (List<Subject>) pageContext.getRequest().getAttribute("subjects")) {
                                    if(subject.getId() == grade.getSubject().getId()) {
                            %>
                            <option value="<%= subject.getId() %>" selected><%= subject.getName() %></option>
                            <% } else { %>
                            <option value="<%= subject.getId() %>"><%= subject.getName() %></option>
                            <% }} %>
                        </select>
                        <% } else { %>
                        <%= grade.getSubject().getName() %>
                        <% } %>
                    </td>
                    <td><input class="inputarea" type="number" name="value" step="0.01" min="0" form="<%= updateForm %>" value="<%= grade.getValue() %>" required/></td>
                    <td><%= grade.getCreatedAt().format(CustomDateTimeFormatter.DATE_TIME) %></td>
                    <td><%= grade.getUpdatedAt().format(CustomDateTimeFormatter.DATE_TIME) %></td>
                    <td>
                        <input type="submit" value="delete" form="<%= deleteForm %>">
                        <input type="submit" value="Update" form="<%= updateForm %>">
                    </td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </div>
</div>


</body>
</html>