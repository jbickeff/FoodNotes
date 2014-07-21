<%-- 
    Document   : test1
    Created on : Jul 18, 2014, 6:07:45 PM
    Author     : Jeff
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>First test!</h1>
        <%session.setAttribute("id", "1");%>
        <form method = "Get" action = "getHistoryTest">
            <input type ="submit">
        </form>
    </body>
</html>
