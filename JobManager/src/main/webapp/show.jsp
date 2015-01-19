<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>job管理</title>
</head>
<body>

<table>
	<thead>
		<td>任务名</td>
		<td>任务组</td>
		<td>时间表达式</td>
		<td>状态</td>
		<td>备注</td>
		<td>操作</td>
	</thead>
	<tbody>
		<c:forEach  var="trigger" items="${triggers }">
		<tr>
			<td>${trigger.name }</td>
			<td>${trigger.group }</td>
			<td>${trigger.expression }</td>
			<td>${trigger.status }</td>
			<td>${trigger.desc }</td>
			<td>
				<a href="${trigger.name }/${trigger.group }/stop">暂停</a>
				<a href="${trigger.name }/${trigger.group }/resume">恢复</a>
				<a href="${trigger.name }/${trigger.group }/del">删除</a>
				<a href="modify">修改表达式</a>
				<a href="${trigger.name }/${trigger.group }/startNow">立即运行一次</a>
			</td>
		</tr>
		</c:forEach>
	</tbody>
</table>
</body>
</html>