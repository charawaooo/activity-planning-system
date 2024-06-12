<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>找回密码</title>
    <base href="<%=basePath%>">
    <title>${title}</title>
    <style>
        body {
            background-image: url("${pageContext.request.contextPath}/themes/nzblue/images/back.jpg"); /* 设置背景图片 */
            background-size: cover; /* 背景图片覆盖整个页面 */
            background-repeat: no-repeat; /* 背景图片不重复 */
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .container {
            width: 400px;
            padding: 20px;
            background-color: rgba(255, 255, 255, 0.9);
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        h2 {
            font-size: 24px;
            margin-bottom: 20px;
            text-align: center;
        }
        label {
            font-weight: bold;
        }
        input[type="text"],
        input[type="email"] {
            width: 100%;
            padding: 10px;
            margin-bottom: 20px;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
        }
        button[type="submit"] {
            width: 100%;
            padding: 10px;
            margin-bottom: 20px;
            background-color: #007bff;
            color: #fff;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }
        button[type="submit"]:hover {
            background-color: #b9f5b9;
        }
    </style>
</head>
<body>
<div class="container">
<h2>找回密码</h2>
<form action="${pageContext.request.contextPath}/users/sendPasswordResetEmail.action" method="post">
    <div>
        <label for="username">用户账号：</label>
        <input type="text" id="username" name="username" required />
    </div>
    <div>
        <label for="contact">邮箱地址：</label>
        <input type="email" id="contact" name="contact" required />
    </div>
    <div>
        <button type="submit">发送邮件</button>
    </div>
    <div>
        <!--返回登录页面-->
        <button type="submit" onclick="window.location.href='<%=basePath%>users/login.jsp'">返回</button>
    </div>
</form>
</div>
<c:if test="${not empty message}">
    <script>
        alert('${message}');
    </script>
</c:if>
</body>
</html>
