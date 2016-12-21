<%@ page contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page isELIgnored="false"%>

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

<%-- $Id: edit.jsp,v 1.1 2009/05/11 02:51:09 lijj Exp $ --%>
<%-- $Source: /cvsroot/project2/IMIS/WebRoot/admin/edit.jsp,v $ --%>
<%-- $Name:  $ --%>

<%@ page import="java.util.Date"%>
<%@ page import="java.util.List"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Collection,java.util.Iterator"%>
<%@ page
	import="org.apache.solr.common.SolrDocumentList,org.apache.solr.common.SolrDocument,java.util.Map"%>
<%@ page import="org.apache.solr.request.SolrRequestHandler"%>
<%@ page import="org.apache.solr.handler.ReplicationHandler"%>

<%-- jsp:include page="header.jsp"/ --%>
<%-- do a verbatim include so we can use the local vars --%>

<%@include file="header.jsp"%>
<script language="javascript" type="text/javascript"
	src="js/My97DatePicker/WdatePicker.js"></script>

<br clear="all">

<!-- ###############################solr的配置信息########################################## -->
<%
	boolean replicationhandler = !core.getRequestHandlers(
			ReplicationHandler.class).isEmpty();
%>
<table>
	<tr>
		<td>
			<h3>
				Solr
			</h3>
		</td>
		<td>
			<%
				if (null != core.getSchemaResource()) {
			%>
			[
			<a
				href="file/?contentType=text/xml;charset=utf-8&file=<%=core.getSchemaResource()%>">Schema</a>]
			<%
				}
				if (null != core.getConfigResource()) {
			%>
			[
			<a
				href="file/?contentType=text/xml;charset=utf-8&file=<%=core.getConfigResource()%>">Config</a>]
			<%
				}
			%>
			[
			<a href="analysis.jsp?highlight=on">Analysis</a>] [
			<a href="schema.jsp">Schema Browser</a>]
			<%
				if (replicationhandler) {
			%>[
			<a href="replication/index.jsp">Replication</a>]<%
				}
			%>
			<br>
			[
			<a href="stats.jsp">Statistics</a>] [
			<a href="registry.jsp">Info</a>] [
			<a href="distributiondump.jsp">Distribution</a>] [
			<a href="ping">Ping</a>] [
			<a href="logging">Logging</a>]
		</td>
	</tr>

	<%-- List the cores (that arent this one) so we can switch --%>
	<%
		org.apache.solr.core.CoreContainer cores = (org.apache.solr.core.CoreContainer) request
				.getAttribute("org.apache.solr.CoreContainer");
		if (cores != null) {
			Collection<String> names = cores.getCoreNames();
			if (names.size() > 1) {
	%><tr>
		<td>
			<strong>Cores:</strong>
			<br>
		</td>
		<td>
			<%
				String url = request.getContextPath();
						for (String name : names) {
							String lname = name.length() == 0 ? cores
									.getDefaultCoreName() : name; // use the real core name rather than the default
							if (name.equals(core.getName())) {
			%>[<%=lname%>]<%
				} else {
			%>[
			<a href="<%=url%>/<%=lname%>/admin/"><%=lname%></a>]<%
				}
						}
			%>
		</td>
	</tr>
	<%
		}
		}
	%>

	<tr>
		<td>
			<strong>App server:</strong>
			<br>
		</td>
		<td>
			[
			<a href="get-properties.jsp">Java Properties</a>] [
			<a href="threaddump.jsp">Thread Dump</a>]
			<%
			if (enabledFile != null)
				if (isEnabled) {
		%>
			[
			<a href="action.jsp?action=Disable">Disable</a>]
			<%
			} else {
		%>
			[
			<a href="action.jsp?action=Enable">Enable</a>]
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

	<form method="GET" action="EditIndex.so" accept-charset="UTF-8">
		<table border="2">
			<%
				SolrDocument solrDocument = (SolrDocument) request.getAttribute("solrDocument");
				Iterator it = solrDocument.iterator();
				SimpleDateFormat myfmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				while (it.hasNext()) {
					Map.Entry entry = (Map.Entry) it.next();
					
					Object key = entry.getKey();
					Object value = entry.getValue();
			%>
			<tr>
				<td><%=key%></td>
				<%
					if ("id".equals(key.toString())) {
				%>
				<td>
					<input type="hidden" name="<%=key%>" value="<%=value%>"><%=value%></td>
				<%
					} else {
							if ("update_time".equals(key)) {
								value = myfmt.format(entry.getValue());
				%><td>
					<input name="<%=key%>" value="<%=value%>" onfocus="new WdatePicker(this,'%Y-%M-%D %h:%m:%s',true)">
				</td>
				<%
					} else {
				%>
				<td>
					<input name="<%=key%>" value="<%=value%>">
				</td>
				<%
					}
						}
				%>
			</tr>
			<%
				}
			%>
			<tr>
				<td>
					请输入权重值
				</td>
				<td>
					<input type="text" name="grade" >
				</td>
			</tr>
			<tr>
				<td></td>
				<td>
					<input type="hidden" name="rows" value='${rows }'>
					<input type="hidden" name="start" value='${start }'>
					<input type="hidden" name="q" value='<%=request.getParameter("q")%>'>
					<input type="hidden" name="pageNo" value='${pageNo}'>
					<input type="hidden" name="pageName" value=<%=request.getParameter("pageName")%>>
					
					<input type="submit" value="更新" />
					<input type="button" value="返回" onclick="history.go(-1)">
				</td>
			</tr>
		</table>
		<p>
	</form>

	<table>
		<tr>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>
			</td>
			<td>
				Current Time:
				<%=new Date()%>
			</td>
		</tr>
		<tr>
			<td>
			</td>
			<td>
				Server Start At:
				<%=new Date(core.getStartTime())%>
			</td>
		</tr>
	</table>
	</body>
	</html>