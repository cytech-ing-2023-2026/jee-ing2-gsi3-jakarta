<div class="topdiv">
    <p><%= title %></p> <!-- Can add the username here -->
    <div class="topMenuContainer">

        <%
            if(pageContext.getSession().getAttribute("user") != null){
        %>

            <img src="${pageContext.request.contextPath}/pictures/HomeDark.png" alt="Home icon" class="icon" id="home" onclick="window.location.href='${pageContext.request.contextPath}/'">
            <img src="${pageContext.request.contextPath}/pictures/LogoutDark.png" alt="Logout icon" class="icon" id="logout">

        <%
            }
        %>
    </div>
</div>
<div class="message_container">
    <%
        String error = (String) pageContext.getAttribute("error");
        if(error == null) error = (String) request.getAttribute("error");
        if(error == null) error = "";

        String message = (String) pageContext.getAttribute("message");
        if(message == null) message = (String) request.getAttribute("message");
        if(message == null) message = "";
    %>
    <div class="message"><%= message %></div>
    <div class="error"><%= error %></div>
</div>
