<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>   
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<style type="text/css">
.error {
    color: red;
    font-style: italic;
}
</style>


<title>Transfer Account</title>
</head>
<body>
	
	<a href="/init">Initialize Accounts</a>
	
	<form:form action="/send" method="POST">
    	From Account : 
    					<input type="text" name="fromAcctID" />
    	To Account : 
    				<input type="text" name="fromAcctID" />
    	Amount :
    			<input type="text" name="fromAcctID" />
    	<input type="submit" value="Submit" />
    </form:form>
    
	<h1 align="center">Account List</h1>
    <br/>
    <table border="1">
        <tr>
            <th>Account ID</th><th>Amount</th><th>Country</th>
        </tr>
        <c:forEach var="account" items="${accounts}">
        
        <tr>
            <td>${account.accountID}</td>
            <td>${account.amount}</td>
        </tr>    
        </c:forEach>
    </table>
    
   
    
    
</body>
</html>