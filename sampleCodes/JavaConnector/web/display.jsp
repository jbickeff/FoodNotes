<%-- 
    Document   : display
    Created on : Jun 30, 2014, 4:41:58 PM
    Author     : Jeff
--%>

<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <p>
        <%
            List<String> list = (List<String>) request.getAttribute("personList");
            for(String p : list)
            {
                response.getWriter().write(p);
            }
        %></p>
    </body>
</html>
