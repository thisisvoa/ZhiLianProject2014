<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="initial-scale=1.1,user-scalable=no">
<title>Info</title>

<script type="text/javascript"
	src="http://webapi.amap.com/maps?v=1.3&key=8e8bb07fd2e349baad06fd735bf8ebe1">
	
</script>

<script type="text/javascript">
	function initialize() {
		var position = new AMap.LngLat(
				<c:out value="${info.location }"></c:out>);
		var mapObj = new AMap.Map("container", {
			view : new AMap.View2D({
				center : position,
				zoom : 12,
				rotation : 0
			}),
			lang : "zh_cn"
		});

		var marker = new AMap.Marker({
			position : mapObj.getCenter(),
			draggable : true,
			cursor : 'move',
			raiseOnDrag : true
		});

		marker.setMap(mapObj);

		AMap.event.addListener(marker, 'dragend', function(e) {
			var location_new = marker.getPosition();
			document.getElementById("location").value = location_new;
		});
	}
</script>

</head>
<body onload="initialize()">
	<p align="right">
		<a href="InfoShowServlet?show=info">简介</a> | <a
			href="DemandShowServlet?show=demand">需求</a> | <a href="welcome.jsp">注销</a>
	</p>

	<hr />
	<form action="InfoEditServlet" method="post">
		<table style="width: 100%;" border="1" cellpadding="5" cellspacing="0">
			<tr>
				<td width="80px">名称</td>
				<td><input style="width: 98%;" type="text" name="name"
					value="<c:out value="${info.name }"></c:out>"></td>
			</tr>
			<tr>
				<td>地址</td>
				<td><input style="width: 98%;" type="text" name="address"
					value="<c:out value="${info.address }"></c:out>"></td>
			</tr>
			<tr>
				<td>坐标</td>
				<td><input style="width: 98%;" type="text" name="location"
					id="location" value="<c:out value="${info.location }"></c:out>"
					readonly></td>
			</tr>
			<tr>
				<td>地图</td>
				<td id="container" height="300px"></td>
			</tr>
			<tr>
				<td>邮箱</td>
				<td><input style="width: 98%;" type="text" name="email"
					value="<c:out value="${info.email }"></c:out>"></td>
			</tr>
			<tr>
				<td>更多信息</td>
				<td id="detail" height="200px"><textarea name="detail"
						style="width: 98%; height: 100%"><c:out
							value="${info.detail }"></c:out></textarea></td>
			</tr>
		</table>

		<p align="center">
			<input type="submit" value="提交修改">
		</p>
	</form>
</body>
</html>