<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!-- jstl标签库 -->
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" rel="stylesheet" href="../css/iframe.css"
	media="all" />
<script type="text/javascript" src="../js/myjs.js"></script>
<script type="text/javascript" src="../js/update_jsp.js"></script>
</head>
<body>
	<div>
		<h2 id="h2">修改用户信息</h2>
		<form class="h">
			<div>
				<label>用户ID:</label> 
				<input type="text" name="id" id="id" value="${updatePerson.id }" readonly="readonly" />
			</div>
			<div>
				<label>用户名:<span style="color: red">*</span></label> <input
					type="text" name="name" id="name" value="${updatePerson.name}" readonly="readonly" />
			</div>
			<div>
				<label>密码:<span style="color: red">*</span></label> <input
					type="password" name="password" id="password" value="${updatePerson.password }" maxlength="6" />
			</div>
			<div>
				<label>电话:<span style="color: red">*</span></label> <input
					type="text" name="phone" id="phone" value="${updatePerson.phone }" maxlength="11" />
			</div>
			<div>
				<label>邮箱:<span style="color: red">*</span></label> <input
					type="email" name="email" id="email" value="${updatePerson.email }" />
			</div>
			<div>
				<label>地址:</label> 
				<input type="text" name="address" id="address" value="${updatePerson.address }" />
			</div>
			<div>
				<label>身份:</label> 
				<input type="text" name="roleName" id="roleName" value="${updatePersonRole.roleName==null?'未添加':updatePersonRole.roleName }" maxlength="10" />
			</div>
			<div>
				<label>负责人:</label> 
				<input type="text" name="head" id="head" value="${updatePerson.head==null?'master':updatePerson.head }"  maxlength="8"/>
			</div>
			<div>
				<label id="warn">注意：<span style="color: red">*</span>选项为必填项
				</label>
				<div class="clear"></div>
			</div>
			<br />
			<div>
				<label></label>
				<div class="clear" id="msg" style="color: red;" ></div>
			</div>
			<div class="button_wrapper">
				<input type="button" class="button" value="确认修改"
					onclick="askUpdate();" /> 
			</div>
			<div class="button_wrapper">
					<input type="button" class="button" value="返回页面"
					onclick="goBack();" />
			</div>
		</form>
	</div>
</body>
</html>
