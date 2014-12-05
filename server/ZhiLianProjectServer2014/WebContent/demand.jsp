<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Demand</title>
<script>
function callservlet()
{
	window.location.href="DemandShowServlet?show=demandadd";
}
</script>
</head>
<body>
	<p align="right">
		<a href="InfoShowServlet?show=info">简介</a> | <a href="DemandShowServlet?show=demand">需求</a> | <a
			href="welcome.jsp">注销</a>
	</p>

	<hr />
	
	<table style="width: 100%;" border="1" cellpadding="5" cellspacing="0">
		<tr>
			<th>云id</th>
			<th>需求领域</th>
			<th>需求职位</th>
			<th>职位名称</th>
			<th>最低月薪</th>
			<th>最高月薪</th>
			<th>需求经验(年)</th>
			<th>更多信息</th>
			<th>操作</th>
		</tr>
		<c:forEach var="demand" items="${list }">
			<tr>
				<td><c:out value="${demand.id_cloud }"></c:out></td>
				<td><c:out value="${demand.demand_field }"></c:out></td>
				<td><c:out value="${demand.demand_job }"></c:out></td>
				<td><c:out value="${demand.demand_jobname }"></c:out></td>
				<td><c:out value="${demand.demand_salary_min }"></c:out></td>
				<td><c:out value="${demand.demand_salary_max }"></c:out></td>
				<td><c:out value="${demand.demand_experience }"></c:out></td>
				<td><p>
					<c:out value="${demand.demand_detail }" escapeXml="false"></c:out>
				</p></td>
				<td nowrap="nowrap"><a href="DemandShowServlet?show=demandedit&id_cloud=<c:out value="${demand.id_cloud }"></c:out>">修改</a> <a href="DemandDelServlet?id_cloud=<c:out value="${demand.id_cloud }"></c:out>">删除</a></td>
			</tr>
		</c:forEach>
	</table>

	<p align="center">
		<button onClick="callservlet()">添加需求</button>
	</p>


</body>
</html>