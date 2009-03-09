<%@page session="false"%>
<jsp:useBean id="pageName" type="String" scope="request"/>
<html>
<head>
      <title>OurPlace: I couldn't update <%=pageName%></title>
      <base href="http://<%=request.getServerName()%>:<%=request.getServerPort()%><%=request.getContextPath()%>/">
<style>
      span.header {font-size: 14px;
                      font-weight: bold;
                      font-family: arial, helvetica, ms sans serif, sans-serif;}
</style>
</head>
<body bgcolor=white text=black vlink=#000066 link=#336699 alink=#333333>
<jsp:include page="/header.jsp" flush="true"/>
<h1>Failure</h1>
<p>You could not save your changes to <a href="<%=pageName%>"><%=pageName%></a>.<br />
Someone else has modified it since you started editing.</p>
</body>
</html>
