<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Forgot Password</title>
</head>
<body>

<h2>Reset Password</h2>

<form action="forgotPassword" method="post">

    Email: <input type="email" name="email" required><br><br>
    New Password: <input type="password" name="password" required><br><br>

    <button type="submit">Reset Password</button>

</form>

</body>
</html>