<%@ page contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ page import="org.apache.solr.core.SolrCore"%>
<%@ page import="java.util.Collection"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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

<html>
	<head>
		<link rel="stylesheet" type="text/css" href="solr-admin.css">
		<link rel="icon" href="favicon.ico" type="image/ico"></link>
		<link rel="shortcut icon" href="favicon.ico" type="image/ico"></link>
		<title>管理员登入</title>
	<style>
			body {
				TEXT-ALIGN: center;
				background-color: #bbbbbb;
			}
		</style>
	</head>
	<body >
			<form action="Login" method="post">
				<table border="1" align="center"  cellpadding="0" cellspacing="0" style="margin-top:280px;margin-left:600px">
					<tr>
						<td colspan="2" align="center" style="font-weight: bolder">
							管理员登入
						</td>
					</tr>
					<tr style="width: 30px">
						<td></td>
						<td></td>
					</tr>
					<tr>
						<td>
							用户名：
						</td>
						<td>
							<input id="uname" name="uname">
						</td>
					</tr>
					<tr>
						<td>
							密&nbsp;&nbsp;码：
						</td>
						<td>
							<input type="password" name="pwd">
						</td>
					</tr>
					<c:if test="${not empty message}">
						<tr>
							<td colspan="2" align="center">
								<font color='red'>${message}</font>
							</td>
						</tr>
					</c:if>
					<tr>
						<td>&nbsp;</td>
						<td>
							<input type="submit" value="提交">
							<input type="reset" value="重置">
						</td>
					</tr>
				</table>
			</form>
	</body>
	<script type="text/javascript">
  			window.onload=function(){
  					document.getElementById("uname").focus();
  			}
  </script>
</html>
