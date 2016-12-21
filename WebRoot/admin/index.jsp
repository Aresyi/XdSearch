<%@ page contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%--
 Licensed to the Apache Software Foundation (ASF) under one or more
 contributor license agreements.  See the NOTICE file distributed with
 this work for additional information regarding copyright ownership.
 The ASF licenses this file to You under the Apache License, Version 2.0
 (the "License"); you may not use this file except in compliance with
 the License.  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
--%>

<%-- $Id: index.jsp 1089340 2011-04-06 07:49:14Z uschindler $ --%>
<%-- $Source: /cvs/main/searching/SolrServer/resources/admin/index.jsp,v $ --%>
<%-- $Name:  $ --%>

<%@ page import="java.util.Date"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Collection"%>
<%@ page import="org.apache.solr.request.SolrRequestHandler"%>
<%@ page import="org.apache.solr.handler.ReplicationHandler"%>


<script type="text/javascript" src="js/ajaxDel.js"></script>
<script type="text/javascript" src="js/jquery.js"></script>


<%-- jsp:include page="header.jsp"/ --%>
<%-- do a verbatim include so we can use the local vars --%>
<%@include file="header.jsp" %>

<%
	boolean replicationhandler = !core.getRequestHandlers(
			ReplicationHandler.class).isEmpty();
%>

<br clear="all">

<!-- ###############################solr的配置信息########################################## -->
<table>
<tr>
  <td>
	<h3>Solr</h3>
  </td>
  <td>
    <%
    	if (null != core.getSchemaResource()) {
    %>
    [<a href="file/?contentType=text/xml;charset=utf-8&file=<%=core.getSchemaResource()%>">Schema</a>]
    <%
    	}
    	if (null != core.getConfigResource()) {
    %>
    [<a href="file/?contentType=text/xml;charset=utf-8&file=<%=core.getConfigResource()%>">Config</a>]
    <%
    	}
    %>
    [<a href="analysis.jsp?highlight=on">Analysis</a>]
    [<a href="schema.jsp">Schema Browser</a>] <%
    	if (replicationhandler) {
    %>[<a href="replication/index.jsp">Replication</a>]<%
    	}
    %>
    <br>
    [<a href="stats.jsp">Statistics</a>]
    [<a href="registry.jsp">Info</a>]
    [<a href="distributiondump.jsp">Distribution</a>]
    [<a href="ping">Ping</a>]
    [<a href="logging">Logging</a>]
  </td>
</tr>

<%-- List the cores (that arent this one) so we can switch --%>
<%
String current = "";

	org.apache.solr.core.CoreContainer cores = (org.apache.solr.core.CoreContainer) request
			.getAttribute("org.apache.solr.CoreContainer");
	if (cores != null) {
		Collection<String> names = cores.getCoreNames();
		if (names.size() > 1) {
%><tr><td><strong>Cores:</strong><br></td><td><%
	String url = request.getContextPath();
			for (String name : names) {
				String lname = name.length() == 0 ? cores
						.getDefaultCoreName() : name; // use the real core name rather than the default
				if (name.equals(core.getName())) {
					current = lname;
%>[<%=lname%>]<%
	} else {
%>[<a href="<%=url%>/<%=lname%>/admin/"><%=lname%></a>]<%
	}
			}
%></td></tr><%
	}
	}
%>

<tr>
  <td>
    <strong>App server:</strong><br>
  </td>
  <td>
    [<a href="get-properties.jsp">Java Properties</a>]
    [<a href="threaddump.jsp">Thread Dump</a>]
  <%
  	if (enabledFile != null)
  		if (isEnabled) {
  %>
  [<a href="action.jsp?action=Disable">Disable</a>]
  <%
  	} else {
  %>
  [<a href="action.jsp?action=Enable">Enable</a>]
  <%
  	}
  %>
  </td>
</tr>


<%
	// a quick hack to get rid of get-file.jsp -- note this still spits out invalid HTML
	out.write(org.apache.solr.handler.admin.ShowFileRequestHandler
			.getFileContents("admin-extra.html"));
%>

</table>
<P>

	<!-- ####################################查询表单####################################### -->
<table>
<tr>
  <td>
	<h3>Make a Query</h3>
  </td>
  <td>
	[<a href="form.jsp">Full Interface</a>]
  </td>
  
</tr>
<tr>
  <td>
  Query String:
  </td>
  <td colspan=2>
	<form name="queryForm" method="GET" action="SearchServlet.so" accept-charset="UTF-8">
        <input class="std" id="q" name="q" value="${ q == null ? defaultSearch :q }">
       
        <input name="version" type="hidden" value="2.2">
		<input name="pageName" type="hidden" value="index.jsp">
		<input name="start" type="hidden" value="0">
		<input name="rows" type="hidden" value="10">
		<input name="indent" type="hidden" value="on">
		<input name="pageNo" type="hidden" value="1">
        <br>
        <br>
        <input class="stdbutton" type="submit" value="search" 
        	   onclick="if (queryForm.q.value.length==0) { alert('no empty queries, please'); return false; } else { queryForm.submit(); return false;} ">
	</form>
  </td>
</tr>
</table>
<p>


<!-- ###################################查询结果展示####################################### -->

<c:if test="${result!=null}">
	<!-- ################################ 分页 ################################ -->
	<div>
		<div style="float: right">
			约有<font color="red">${num }</font>项符合【${q }】 的查询结果，以下是第${rows*(pageNo-1)+1 }-${ rows*pageNo > num ? num : (rows*pageNo) }项 |
			&nbsp;
			<%
				Object _query = request.getAttribute("q");
					if (_query != null)
						_query = _query.toString().replace('&', '@');
					pageContext.setAttribute("_q", _query.toString());
			%>
			<c:set var="_ref"
				value='SearchServlet.so?pageName=${"index.jsp"}&pageNo=${pageNo-1 }&rows=${rows}&q=${_q }' />
			<a href='${pageNo-1<1?"#":_ref }'>上一页</a>&nbsp;&nbsp;
			<c:set var="ref"
				value='SearchServlet.so?pageName=${"index.jsp"}&pageNo=${pageNo+1 }&rows=${rows}&q=${_q }' />
			<a href='${pageNo>num/rows?"#":ref}'>下一页</a>
		</div>
	</div>
	<table border="2">
		<tr>
			<td colspan="400" style="falot:right">
				第${pageNo }页
			</td>
		</tr>
		<c:set var="n" value="1"></c:set>

		<tr>
			<th style="width:5%;"></th>
			<c:forEach items="${tableHead}" var="head">
				<c:set var="n" value="${n+1}" />
				<th >
					${head }
				</th>
			</c:forEach>
			<th width="10%">
				操作
			</th>
		</tr>

		<c:forEach items="${result}" var="doc">

			<c:set var="id" value="" />
			<c:forEach items="${doc}" var="field">
				<c:if test="${field.key=='id'}">
					<c:set var="id" value="${field.value}" />
					<tr id="${field.value}_id">
						<td style="width:5%;">
							<input type="checkbox" id="${field.value}" name="checkbox" />
						</td>
				</c:if>
			</c:forEach>
			
			<c:forEach items="${tableHead}" var="key">
				<c:set var="arg" value="f"></c:set>
				<c:forEach items="${doc}" var="field">
					<c:if test="${field.key eq key}">
						<td style="width:${1/n*85}%;" height="20">
							${field.value }
						</td>
						<c:set var="arg" value="t"></c:set>
					</c:if>
				</c:forEach>
				<c:if test="${arg=='f'}">
					<td style="width:${1/n*85}%;" height="20">
						&nbsp;
					</td>
				</c:if>
			</c:forEach>
			<td style="width:10%;" >
				<a
					href='InitEditIndex.so?pageName="index.jsp"&id=${id}&rows=${rows}&pageNo=${pageNo }&q=${_q }'>修改</a>&nbsp;|&nbsp;
				<a
					href='DeleteIndex.so?pageName=${"index.jsp"}&id=${id}&pageNo=${pageNo }&q=${_q }'
					onclick="javascript:return confirm('是否删除此索引？')">删除</a>
			</td>

			</tr>
		</c:forEach>
		<tr>
			<td colspan="400">
				<input type="checkbox" onclick="selAll(this)" />
				&nbsp;全选 &nbsp;&nbsp;&nbsp;
				<a href="javascript:delsel();">删除选中索引</a>
			</td>
		</tr>

	</table>
		<!-- ################################ 分页 ################################ -->
	<div>
		<div style="float: right">
			约有<font color="red">${num }</font>项符合【${q }】 的查询结果，以下是第${rows*(pageNo-1)+1 }-${ rows*pageNo > num ? num : (rows*pageNo) }项 |
			&nbsp;
			<%
					if (_query != null)
						_query = _query.toString().replace('&', '@');
					pageContext.setAttribute("_q", _query.toString());
			%>
			<c:set var="_ref"
				value='SearchServlet.so?pageName=${"index.jsp"}&pageNo=${pageNo-1 }&rows=${rows}&q=${_q }' />
			<a href='${pageNo-1<1?"#":_ref }'>上一页</a>&nbsp;&nbsp;
			<c:set var="ref"
				value='SearchServlet.so?pageName=${"index.jsp"}&pageNo=${pageNo+1 }&rows=${rows}&q=${_q }' />
			<a href='${pageNo>num/rows?"#":ref}'>下一页</a>
		</div>
	</div>
	<p/>
</c:if>


<!-- 
<table>
<tr>
  <td>
	<h3>TEST</h3>
  </td>
  <td>
	[<a href="test_suggest.jsp">测试智能提示 ydj</a>]
  </td>
</tr>
</table>
 -->
<p>


<table>
<tr>
  <td>
	<h3>Assistance</h3>
  </td>
  <td>
	[<a href="http://lucene.apache.org/solr/">Documentation</a>]
	[<a href="http://issues.apache.org/jira/browse/SOLR">Issue Tracker</a>]
	[<a href="mailto:solr-user@lucene.apache.org">Send Email</a>]
	[<a href="http://wiki.apache.org/solr/SolrQuerySyntax">Solr Query Syntax</a>]
  </td>
</tr>
</table>



<table>
	<tr>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td></td>
		<td>
			Current Time:
			<%=new Date()%></td>
	</tr>
	<tr>
		<td></td>
		<td>
			Server Start At:
			<%=new Date(core.getStartTime())%></td>
	</tr>
</table>




</body>
</html>
