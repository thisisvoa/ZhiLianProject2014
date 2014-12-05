<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body bgcolor="#EEF4EA" >
<div align="center">
	<br />
	<p>
		<span style="font-size:32px;color:#337FE5;">职连招聘者信息录入系统</span>
	</p>
	<br />
	
	<hr />
	
	<br />
	
	<form method="post" action="RegisterServlet" >
		<table >
			<tr>
				<td align="center">帐号</td>
				<td align="center"><input name="username" type="text" /> 
				</td>
			</tr>
			<tr >
				<td align="center">密码</td>
				<td align="center"><input name="userpassword" type="text" />
				</td>
			</tr>
			
			
			<tr >
			    <td></td>
				<td align="center"><input type="submit" value="注册"/></td>
			</tr>
		</table>
	</form>
</div>
</body>
</html>