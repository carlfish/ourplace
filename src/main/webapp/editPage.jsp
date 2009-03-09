<%@page session="false"%>
<jsp:useBean id="wikiPage" type="org.pastiche.wiki.Page" scope="request"/>
<html>
<head>
	<title>Edit <jsp:getProperty name="wikiPage" property="name"/></title>
<style>
      span.header {font-size: 14px;
                      font-weight: bold;
                      font-family: arial, helvetica, ms sans serif, sans-serif;}
</style>
</head>
<body bgcolor=white text=black vlink=#000066 link=#336699 alink=#333333>
<jsp:include page="/header.jsp" flush="true"/>
<form method="POST" action="<%=request.getContextPath()%>/">
<h1><jsp:getProperty name="wikiPage" property="name"/> <input type="submit" value=" Save "></h1>
<p>Summary: <input type="text" size=40 name="summary"
value="<%=wikiPage.getSummary()%>"></p>

<textarea name="text" rows=18 cols=80 wrap=virtual>
<jsp:getProperty name="wikiPage" property="content"/>
</textarea>
<br />
<input type="hidden" name="version" value="<%=wikiPage.getVersion()%>" />
<input type="checkbox" name="convert" value="tabs" />
I can't type tabs. Please <a href="<%=request.getContextPath()%>/ConvertSpacesToTabs">ConvertSpacesToTabs</a> for me when I save.<br />
<input type="checkbox" name="minorEdit" value="yes" />
I'm only making a minor change to the page. List this on 
<a href="<%=request.getContextPath()%>/RecentEdits">RecentEdits</a> instead of 
<a href="<%=request.getContextPath()%>/RecentChanges">RecentChanges</a><br />
<a href="<%=request.getContextPath()%>/GoodStyle">GoodStyle</a> tips for editing.<br />
<input type="hidden" size=1 name="name" value="<%=wikiPage.getName()%>" />
</form>
</body>
</html>
