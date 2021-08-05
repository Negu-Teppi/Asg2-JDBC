<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Home Page</title>
    <style>
        table, th, td {
            text-align: center;
        }
        #view td,th  {
            border: 1px solid black;
        }
        #view {
            border-collapse: collapse;
            width: 70%;
        }
        td th {
            width: 100px;
        }
    </style>
</head>
<body>
<a href="./add">Add</a>
<form action="load" method="get">
    <table>
        <tr>
            <td>Name</td>
            <td><input type="text" name="name" value="${sessionScope.name}"/></td>
        </tr>
        <tr>
            <td>Salary</td>
            <td><input type="text" name="salary"  value="${sessionScope.salary}"/></td>
        </tr>
        <tr>
            <td>Department</td>
            <td><input type="text" name="department" value="${sessionScope.departmentName}"/></td>
        </tr>
        <tr>
            <td><input type="submit" value="Search"/></td>
        </tr>
    </table>
</form>
<br/>
<table id="view">
    <tr>
        <th>Id</th>
        <th>Name</th>
        <th>Salary</th>
        <th>Department Name</th>
    </tr>
    <c:forEach var="employee" items="${employeeList}">
        <tr>
            <td>${employee.id}</td>
            <td>${employee.name}</td>
            <td>${String.format("%.0f", employee.salary)}</td>
            <td style="width: 20px">${employee.getDepartmentName()}</td>
            <td><a href="./del?id=${employee.id}">del</a></td>
            <td><a href="./edit?id=${employee.id}">edit</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>