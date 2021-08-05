<%--
  Created by IntelliJ IDEA.
  User: Hung_LV
  Date: 8/5/2021
  Time: 3:46 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Add Page</title>
</head>
<body>
    <form action="add" method="post">
        <table>
            <tr>
                <td>Name</td>
                <td><input type="text" name="name"/></td>
            </tr>
            <tr>
                <td>Salary</td>
                <td><input type="text" name="salary"/></td>
            </tr>
            <tr>
                <td>Deparment Name</td>
                <td>
                    <select id="departmentName" name="departmentName">
                        <c:forEach var="department" items="${departmentList}">
                            <option value="${department.departmentName}">${department.departmentName}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td><input type="submit" colspan="2" value="Save"></td>
            </tr>
        </table>
    </form>
</body>
</html>
