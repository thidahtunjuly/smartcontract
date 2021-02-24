<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>   
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>get balance</title>
</head>
<body>
	<form:form action="/getAccount" method="POST">
    	<input type="text" name="accountID" />
    	<input type="submit" value="Submit" />
    </form:form>

	<table border="1">
        <tr>
            <th>Account ID</th><th>Amount</th><th>Country</th>
        </tr>
       	<tr>
            <td>${account.accountID}</td>
            <td>${account.amount}</td>
        </tr>    
    </table>
    
    
</body>
</html>