<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="fr.cyu.jee.model.Course" %>
<%@ page import="java.time.LocalDateTime, java.time.LocalDate, java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.Set" %>

<!DOCTYPE html>
<html>
<head>
    <title>Timetable</title>
    <link href="${pageContext.request.contextPath}/css/main.css" rel="stylesheet">
    <!-- Lien vers le fichier CSS principal -->
    <link href="${pageContext.request.contextPath}/css/course/user.css" rel="stylesheet">
    <!-- Lien vers le fichier CSS spécifique aux cours -->
</head>
<body class="main_body">

<% String title = "Timetable"; %>
<%@ include file="banner.jsp" %>

<%
    LocalDate selectedMonday = (LocalDate) request.getAttribute("selectedMonday");
    LocalDate selectedSunday = (LocalDate) request.getAttribute("selectedSunday");
    Set<Course> courses = (Set<Course>) request.getAttribute("courses");

    DateTimeFormatter weekFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
%>

<div class="week-header">
    <button onclick="window.location.href = '?date=<%= selectedMonday.minusWeeks(1).format(weekFormatter) %>'">&lt;</button>
    <h1><%= selectedMonday.format(weekFormatter) %> - <%= selectedSunday.format(weekFormatter) %></h1>
    <button onclick="window.location.href = '?date=<%= selectedMonday.plusWeeks(1).format(weekFormatter) %>'">&gt;</button>
</div>
<div class="timetable">
    <!-- En-têtes des jours de la semaine -->
    <div></div> <!-- Cellule vide pour l'angle en haut à gauche -->
    <div class="day-header">Monday</div>
    <div class="day-header">Tuesday</div>
    <div class="day-header">Wednesday</div>
    <div class="day-header">Thursday</div>
    <div class="day-header">Friday</div>
    <div class="day-header">Saturday</div>
    <div class="day-header">Sunday</div>

    <!-- Créneaux horaires statiques dans la première colonne et cellules vides pour chaque jour -->
    <%
        LocalDateTime startTime = LocalDateTime.of(LocalDate.now(), java.time.LocalTime.of(8, 0)); // Heure de début à 8h00
        DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH:mm");
        for (int i = 0; i < (20 - 8) * 4; i++) { // Boucle pour créer 48 créneaux de 15 minutes (de 8h00 à 20h00)
            if (i % 2 == 0) {
                String timeLabel = startTime.plusMinutes(i * 15).format(hourFormatter); // Formatage de chaque créneau horaire
                String timeStyle = "grid-row-start: " + (i + 2) + "; grid-row-end: " + (i + 4) + ";";
    %>
    <div class="time-slot" style="<%= timeStyle%>"><%= timeLabel %>
    </div> <!-- Affichage du créneau horaire dans la première colonne -->
    <!-- Cellules vides avec bordure pour chaque jour de la semaine -->
    <%
        }
        String cellRow = "quarter-" + i % 4;
        for (int j = 0; j < 7; j++) {
            String cellStyle = "grid-row: " + (i + 2) + ";grid-column: " + (j + 2) + ";";
    %>
    <div class="empty-cell <%= cellRow %>" style="<%= cellStyle %>"></div>
    <% } %>
    <% } %>

    <!-- Affichage des cours -->
    <% for (Course course : courses) {
        int dayColumn = course.getBeginDate().getDayOfWeek().getValue(); // Colonne correspondant au jour de la semaine (Lundi = 1, Mardi = 2, etc.)
        int beginHour = (course.getBeginDate().getHour() - 8) * 4 + course.getBeginDate().getMinute() / 15; // Calcul de la position de début du cours dans la grille
        int durationSlots = (int) course.getDuration().toMinutes() / 15; // Calcul de la durée du cours en créneaux de 15 minutes
        String cellStyle = "grid-column: " + (dayColumn + 1) + "; grid-row: " + (beginHour + 2) + " / span " + durationSlots + ";"; // Style CSS dynamique pour positionner le cours dans la grille
    %>
    <div class="course" style="<%= cellStyle %>">
        <label class="subject"><%= course.getSubject().getName() %></label>
        <label class="hour"><%= course.getBeginDate().format(hourFormatter) %> - <%= course.getEndDate().format(hourFormatter) %></label>
        <label class="teacher"><%= course.getTeacher().getFirstName() + " " + course.getTeacher().getLastName().toUpperCase() %></label>
    </div> <!-- Affichage du cours avec son style positionné dans la grille -->
    <% } %>
</div>

</body>
</html>
