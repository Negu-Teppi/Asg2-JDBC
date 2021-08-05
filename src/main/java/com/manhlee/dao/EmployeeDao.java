package com.manhlee.dao;

import com.manhlee.helper.ConnectionPool;
import com.manhlee.model.Department;
import com.manhlee.model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDao {
    public static List<? extends Department> findAll() {
        String query = "SELECT e.id `id`, e.name `name`, e.salary `salary`, d.name `departmentName`" +
                " FROM employee e INNER JOIN department d on e.departmentId = d.id";
        List<Employee> employeeList = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        try (
                PreparedStatement pstmt = connection.prepareStatement(query.toString());
                ResultSet resultSet = pstmt.executeQuery();
        ) {
            while (resultSet.next()) {
                Employee employee = new Employee(resultSet.getInt("id"),
                        resultSet.getString("name"), resultSet.getDouble("salary"),
                        resultSet.getString("departmentName"));
                employeeList.add(employee);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            pool.freeConnection(connection);
        }
        return employeeList;
    }

    public static List<? extends Department> findAllAndFindByCondition(String name, double salary, String departmentName) {
        StringBuilder query = new StringBuilder("SELECT e.id `id`, e.name `name`,");
        query.append(" e.salary `salary`, d.name `departmentName`");
        query.append(" FROM employee e");
        query.append(" INNER JOIN department d on e.departmentId = d.id");
        if (name == null) {
            name = "";
        }
        if (departmentName == null) {
            departmentName = "";
        }
        if (!name.isEmpty() || !departmentName.isEmpty() || salary > 0) {
            query.append(" WHERE");
            if (name.isEmpty() && departmentName.isEmpty()) {
                query.append(" e.salary = ?");
            } else if (!name.isEmpty() && departmentName.isEmpty()) {
                if (salary > 0) {
                    query.append(" e.name LIKE CONCAT( '%',?,'%') AND e.salary = ?");
                } else {
                    query.append(" e.name LIKE CONCAT( '%',?,'%')");
                }
            } else if (name.isEmpty() && !departmentName.isEmpty()) {
                if (salary > 0) {
                    query.append(" d.name LIKE CONCAT( '%',?,'%') AND e.salary = ?");
                } else {
                    query.append(" d.name LIKE CONCAT( '%',?,'%')");
                }
            } else {
                query.append(" e.name LIKE CONCAT( '%',?,'%') AND d.name LIKE CONCAT( '%',?,'%') AND salary = ?");
            }
        }
        List<Employee> employeeList = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        try {
            PreparedStatement pstmt = connection.prepareStatement(query.toString());
            if (!name.isEmpty() || !departmentName.isEmpty() || salary > 0) {
                if (name.isEmpty() && departmentName.isEmpty()) {
                    pstmt.setDouble(1, salary);
                } else if (!name.isEmpty() && departmentName.isEmpty()) {
                    if (salary > 0) {
                        pstmt.setString(1, name);
                        pstmt.setDouble(2, salary);
                    } else {
                        pstmt.setString(1, name);
                    }
                } else if (name.isEmpty() && !departmentName.isEmpty()) {
                    if (salary > 0) {
                        pstmt.setString(1, departmentName);
                        pstmt.setDouble(2, salary);
                    } else {
                        pstmt.setString(1, departmentName);
                    }
                } else {
                    pstmt.setString(1, name);
                    pstmt.setString(2, departmentName);
                    pstmt.setDouble(3, salary);
                }
            }
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                Employee employee = new Employee(resultSet.getInt("id"),
                        resultSet.getString("name"), resultSet.getDouble("salary"),
                        resultSet.getString("departmentName"));
                employeeList.add(employee);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            pool.freeConnection(connection);
        }
        return employeeList;
    }

    public static void addEmployee(String name, double salary, String departmentName) {
        String query = "INSERT INTO employee\n" +
                "SET `name` = ?, salary = ?,\n" +
                "departmentId = (\n" +
                "    SELECT id\n" +
                "    FROM department\n" +
                "    WHERE `name` = ?\n" +
                "    )";

        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        try (PreparedStatement pstms = connection.prepareStatement(query.toString());) {

            pstms.setString(1, name);
            pstms.setDouble(2, salary);
            pstms.setString(3, departmentName);
            pstms.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            pool.freeConnection(connection);
        }
    }

    public static Employee findEmployee(int id) {
        String query = "SELECT E.ID, E.NAME, E.SALARY, D.NAME\n" +
                "FROM employee E\n" +
                "INNER JOIN department D ON E.departmentId = D.id\n" +
                "WHERE E.ID = ?";
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        try (PreparedStatement pstmt = connection.prepareStatement(query);

        ){
            pstmt.setInt(1, id);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()){
                Employee employee = new Employee(resultSet.getInt("e.id"),
                        resultSet.getString("e.name"),
                        resultSet.getDouble("e.salary"),
                        resultSet.getString("d.name"));
                return employee;
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            pool.freeConnection(connection);
        }
        return null;
    }

    public static void editEmployee(Employee employee, String departmentName){
        String query ="Update employee\n" +
                "set name = ?, salary = ?, departmentId = (\n" +
                "    SELECT id\n" +
                "    from department\n" +
                "    where department.name =?\n" +
                "    )\n" +
                "Where id= ?";
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        try (PreparedStatement pstmt = connection.prepareStatement(query);

        ){
            pstmt.setString(1, employee.getName());
            pstmt.setDouble(2,employee.getSalary());
            pstmt.setString(3, departmentName);
            pstmt.setInt(4, employee.getId());
            pstmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            pool.freeConnection(connection);
        }
    }

    public static void deleteEmployeeById(int id){
        String query = "DELETE FROM EMPLOYEE WHERE id = ?";
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        try (PreparedStatement pstmt = connection.prepareStatement(query);

        ){
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            pool.freeConnection(connection);
        }
    }
}
