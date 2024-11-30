<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="fr.cyu.jee.model.Grade" %>
<%@ page import="java.util.List" %>
<%@ page import="fr.cyu.jee.util.CustomDateTimeFormatter" %>
<%@ page import="org.hibernate.dialect.function.ListaggGroupConcatEmulation" %>
<!DOCTYPE html>
<html>
<head>
  <title>Grades</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css" />
  <script src="https://html2canvas.hertzen.com/dist/html2canvas.min.js"></script>
  <script src="https://unpkg.com/jspdf@latest/dist/jspdf.umd.min.js"></script>
  <script src="https://unpkg.com/jspdf-autotable@latest/dist/jspdf.plugin.autotable.js"></script>
  <script src="${pageContext.request.contextPath}/js/student_grades.js"></script>
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
    <!-- Liste des notes à droite -->
    <div style="flex: 2; padding: 10px;">
      <h2 id="grades_title">Student Grades</h2>
      <button onclick="saveAsPDF('${sessionScope.user.firstName}', '${sessionScope.user.lastName}')">Download PDF</button>
      <table id="grades_container" class="grades-table">
        <thead>
        <tr>
          <th>Subject</th>
          <th>Grade</th>
          <th>Created At</th>
          <th>Updated At</th>
        </tr>
        </thead>
        <tbody>
        <%
          List<Grade> grades = (List<Grade>) pageContext.getRequest().getAttribute("grades");
          double sum=0;
          for(Grade grade : grades) {
            sum+=grade.getValue();
        %>
        <tr>
          <td><%= grade.getSubject().getName() %></td>
          <td><%= grade.getValue() %></td>
          <td><%= grade.getCreatedAt().format(CustomDateTimeFormatter.DATE_TIME) %></td>
          <td><%= grade.getUpdatedAt().format(CustomDateTimeFormatter.DATE_TIME) %></td>
        </tr>
        <% } %>
        </tbody>
      </table>
      <label>Average: <%=sum/grades.size()%></label>
    </div>
  </div>
</div>


</body>
</html>