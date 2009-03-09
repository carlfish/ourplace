<%@page session="false"%>
<jsp:useBean id="wikiPage" type="org.pastiche.wiki.Page" scope="request"/>
<html>
<head>
      <title>OurPlace: <jsp:getProperty name="wikiPage" property="name"/></title>
      <base href="http://<%=request.getServerName()%>:<%=request.getServerPort()%><%=request.getContextPath()%>/">
<style>
      span.header {font-size: 14px;
      		      font-weight: bold;
      		      font-family: arial, helvetica, ms sans serif, sans-serif;}
</style>
</head>
<body bgcolor=white text=black vlink=#000066 link=#336699 alink=#333333>
<jsp:include page="/header.jsp" flush="true"/>
<h1><a href="search?search=<%=wikiPage.getName()%>"><jsp:getProperty name="wikiPage" property="name"/></a></h1>
<jsp:getProperty name="wikiPage" property="contentAsHtml"/>
<hr />
<p>
<a href="edit?page=<%=wikiPage.getName()%>">EditText</a> of this page.
(last edited <%=new java.text.SimpleDateFormat("MMMM dd, yyyy").format(wikiPage.getLastModified())%>)<br />
<a href="FindPage">FindPage</a> by searching.<br />
</p>
</body>
</html>