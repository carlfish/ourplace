<%@page session="false"%>
<jsp:useBean id="searchTerm" type="java.lang.String" scope="request"/>
<jsp:useBean id="searchResults" type="java.util.SortedSet" scope="request"/>
<jsp:useBean id="pageCount" type="java.lang.Integer" scope="request"/>
<html>
<head>
      <title>Search Results: <%=searchTerm%></title>
      <base href="http://<%=request.getServerName()%>:<%=request.getServerPort()%><%=request.getContextPath()%>/" />
<style>
      span.header {font-size: 14px;
                      font-weight: bold;
                      font-family: arial, helvetica, ms sans serif, sans-serif;}
</style>
</head>
<body bgcolor=white text=black vlink=#000066 link=#336699 alink=#333333>
<jsp:include page="/header.jsp" flush="true"/>
<h1>Search Results: <%=searchTerm%></h1>
<%
	java.util.Iterator i = searchResults.iterator();
	while (i.hasNext()) {
		org.pastiche.wiki.SearchResult result = (org.pastiche.wiki.SearchResult)i.next();
%>
<a href="<%=result.getPage().getName()%>" title="<%=result.getPage().getSummaryWithEscapedQuotes()%>"><%=result.getPage().getName()%></a> . . . . . . . <%=result.getContext()%><br />	
<%
	}
%>
<hr />
<p>
<%=searchResults.size()%> pages found out of <%=pageCount%> pages searched.
</p>
</body>
</html>
