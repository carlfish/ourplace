<%@page session="false"%>
<jsp:useBean id="pageName" type="String" scope="request"/>
<html>
<head>
      <title>OurPlace: Thanks for rewriting <%=pageName%></title>
      <base href="http://<%=request.getServerName()%>:<%=request.getServerPort()%><%=request.getContextPath()%>/">
<style>
      span.header {font-size: 14px;
                      font-weight: bold;
                      font-family: arial, helvetica, ms sans serif, sans-serif;}
</style>
</head>
<body bgcolor=white text=black vlink=#000066 link=#336699 alink=#333333>
<jsp:include page="/header.jsp" flush="true"/>
<h1>Thankyou</h1>
<p>Thankyou for editing <a href="<%=pageName%>"><%=pageName%></a>.<br />
You have just made the web a richer place. (we hope)</p>
<p>p.s. Be sure to <em>Reload</em> your old pages.</p>
</body>
</html>
