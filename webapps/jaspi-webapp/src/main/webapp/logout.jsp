<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*"%>
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.http.*" %>
<html>
<head>
<title>Logout</title>
</head>

<body>
  <%
    HttpSession s = request.getSession(false);
    if (s != null)
      s.invalidate();
   %>
   <h1>Logout</h1>

   <p>You are now logged out.</p>
   <a href="index.html"/>Back to Index</a>
</body>

</html>

