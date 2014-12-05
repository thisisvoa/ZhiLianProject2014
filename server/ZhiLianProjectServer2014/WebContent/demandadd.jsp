<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>DemandAdd</title>
<script>
function changefield() {
	var field_selected = document.getElementById('demand_field').value;
	var select_job = document.getElementById('demand_job');
	select_job.options.length = 1;
	var json_job = ${json_job};
	for(var job in json_job) {
		if (json_job[job].field==field_selected) {
			var option = new Option(json_job[job].job, json_job[job].job);
			select_job.add(option);
		}
	}
}
</script>
</head>
<body>
	<p align="right">
		<a href="InfoShowServlet?show=info">简介</a> | <a
			href="DemandShowServlet?show=demand">需求</a> | <a href="welcome.jsp">注销</a>
	</p>

	<hr />
	<form action="DemandAddServlet" method="post">
		<table style="width: 100%;" border="1" cellpadding="5" cellspacing="0">

			<tr>
				<td width="80px">需求领域</td>
				<td><select id="demand_field" name="demand_field" onChange="changefield()">
						<option value="">-选择需求领域-</option>
						<c:forEach var="field" items="${list_field }">
							<option value="${field }"><c:out value="${field }"></c:out></option>
						</c:forEach>
				</select></td>
			</tr>
			<tr>
				<td>需求职位</td>
				<td><select id="demand_job" name="demand_job">
					<option value="">-选择需求职位-</option>
				</select></td>
			</tr>
			<tr>
				<td>职位名称</td>
				<td><input style="width: 98%;" type="text"
					name="demand_jobname"></td>
			</tr>
			<tr>
				<td>最低月薪</td>
				<td><input style="width: 98%;" type="text"
					name="demand_salary_min"></td>
			</tr>
			<tr>
				<td>最高月薪</td>
				<td><input style="width: 98%;" type="text"
					name="demand_salary_max"></td>
			</tr>
			<tr>
				<td>需求经验(年)</td>
				<td><input style="width: 98%;" type="text"
					name="demand_experience"></td>
			</tr>
			<tr>
				<td>更多信息</td>
				<td height="200px"><textarea name="demand_detail"
						style="width: 98%; height: 100%"></textarea></td>
			</tr>
		</table>

		<p align="center">
			<input type="submit" value="提交">
		</p>
	</form>
</body>
</html>