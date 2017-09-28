<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<c:set var="base" value="${pageContext.request.contextPath }/" scope="session"/>
<sec:authentication property="principal" var="auth" scope="session" />

<html>
<title>登陆界面</title>
<body>
<h2>Hello World!</h2>
<h2>${auth }</h2>
<form action="${base }j_spring_security_check" method="post">
	用户名：<input type="text" name="j_username" /><br/>
	密码：<input type="password" name="j_password" /><br/>
	<input type="submit" value="登录" />
</form>
<a href="${base }j_spring_security_logout">注销</a>
</body>
</html>
