<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!-- jstl标签库 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" rel="stylesheet" href="../css/table.css"
	media="all" />
<script type="text/javascript" src="../js/myjs.js"></script>
<script type="text/javascript" src="../js/ms.js"></script>
<script type="text/javascript" src="../js/listPerson_jsp.js"></script>
</head>
<body>
	<div>
		<h2 class="h2">查看联系人</h2>
		<form id="f1">
			第
			<input name="page" id="page" size="1" value="${pageInfo.page }"
				onblur="sub_page_p(this.value);" />页&nbsp;&nbsp;
			<a href="http://localhost:8088/MS/person/listPerson?page=${pageInfo.page-1}">上一页</a>
			&nbsp;第|<span style="color:red;">${pageInfo.page}</span>|页&nbsp;
			<a href="http://localhost:8088/MS/person/listPerson?page=${pageInfo.page+1}">下一页</a>
			&nbsp;&nbsp;共|<span style="color:red;">${pageInfo.totalpages}</span>|页
			<div style="float: right;">
			每页显示|<span style="color:red;">${pageInfo.pagesize}</span>|条数据
			</div>
		</form>
		<form id="f2" >
			<table>
				<thead>
					<tr>
						<td>选项</td>
						<td>修改</td>
						<td>用户名</td>
						<td>密码</td>
						<td>电话</td>
						<td>邮箱</td>
						<td>地址</td>
						<td>登陆状态</td>
						<td>角色</td>
						<td>负责人</td>
					</tr>
				</thead>
				<c:forEach items="${persons}" var="person">
					<tr>
						<td><input type="checkbox" name="delete" value="${person.id }" /></td>
						<td><input class='btn' type="button" value="${person.id }" onclick="updatePerson(this.value);"></a></td>
						<td>${person.name }</td>
						<td>${person.password }</td>
						<td>${person.phone }</td>
						<td>${person.email }</td>
						<td>${person.address }</td>
						<c:if test="${person.islogin == 'f'}">
							<td>离线</td>
						</c:if>
						<c:if test="${person.islogin == 't'}">
							<td>在线</td>
						</c:if>
						<td>${personRole[person.id]}</td>
						<td>${person.head eq null?'无':person.head}</td>
					</tr>
				</c:forEach>
				<tr >
					<td colspan="10">
						<input type="button" class="button" value="批量删除" onclick="deletePerson();" />
						&nbsp;&nbsp;全选<input type="checkbox"  onclick="selectAll();">
						&nbsp;&nbsp;反选<input type="checkbox"  onclick="selectOther();">
						&nbsp;&nbsp;&nbsp;<span id="msg" style="color:red" ></span>
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>