package com.manhlee.dao;

import com.manhlee.helper.ConnectionPool;
import com.manhlee.model.Department;
import com.manhlee.model.Employee;
import com.manhlee.model.EmployeeEntity;
import com.manhlee.utils.JPAUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDao {

    public static List<Employee> findAll() {
        String query = "SELECT * FROM EMPLOYEE";
        List<Employee> employeeList = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        try (
                PreparedStatement pstmt = connection.prepareStatement(query.toString());
                ResultSet resultSet = pstmt.executeQuery();
        ) {

            while (resultSet.next()) {
                Employee employee =  new Employee();
                employee.setId(resultSet.getInt("id"));
                employee.setName(resultSet.getString("name"));
                employee.setSalary(resultSet.getDouble("salary"));
                Department department = DepartmentDao.findDepartmentById(resultSet.getInt("departmentId"));
                employee.setDepartment(department);
                employeeList.add(employee);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            pool.freeConnection(connection);
        }
        return employeeList;
    }

    public static List<Employee> findAllAndFindByCondition(String name, double salary, String departmentName) {

        StringBuilder query = new StringBuilder("SELECT * FROM Employee e");
        query.append(" INNER JOIN department d on e.departmentId = d.id WHERE 1=1");
        if (name == null) {
            name = "";
        }
        if (departmentName == null) {
            departmentName = "";
        }
        if (!name.isEmpty() || !departmentName.isEmpty() || salary > 0) {
            if (name.isEmpty() && departmentName.isEmpty()) {
                query.append(" AND e.salary = ?");
            } else if (!name.isEmpty() && departmentName.isEmpty()) {
                if (salary > 0) {
                    query.append(" AND e.name LIKE CONCAT( '%',?,'%') AND e.salary = ?");
                } else {
                    query.append(" AND e.name LIKE CONCAT( '%',?,'%')");
                }
            } else if (name.isEmpty() && !departmentName.isEmpty()) {
                if (salary > 0) {
                    query.append(" AND d.name LIKE CONCAT( '%',?,'%') AND e.salary = ?");
                } else {
                    query.append(" AND d.name LIKE CONCAT( '%',?,'%')");
                }
            } else {
                query.append(" AND e.name LIKE CONCAT( '%',?,'%') AND d.name LIKE CONCAT( '%',?,'%') AND salary = ?");
            }
        }
        query.append(" ORDER BY e.id ASC");
        List<Employee> employeeList = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        try (PreparedStatement pstmt = connection.prepareStatement(query.toString());
        ) {
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
                Employee employee = new Employee();
                employee.setId(resultSet.getInt("id"));
                employee.setName(resultSet.getString("name"));
                employee.setSalary(resultSet.getDouble("salary"));
                Department department = DepartmentDao.findDepartmentById(resultSet.getInt("departmentId"));
                employee.setDepartment(department);
                employeeList.add(employee);
            }
            resultSet.close();
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
        StringBuilder query = new StringBuilder("SELECT * FROM employee E");
            query.append(" INNER JOIN department D ON E.departmentId = D.id WHERE E.ID = ?");
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        try (PreparedStatement pstmt = connection.prepareStatement(query.toString());

        ) {
            pstmt.setInt(1, id);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setId(resultSet.getInt("id"));
                employee.setName(resultSet.getString("name"));
                employee.setSalary(resultSet.getDouble("salary"));
                Department department = DepartmentDao.findDepartmentById(resultSet.getInt("departmentId"));
                employee.setDepartment(department);
                return employee;
            }
            resultSet.close();
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
        return null;
    }

    public static void editEmployee(int id, String name, double salary, String departmentName) {
        StringBuilder query = new StringBuilder("Update employee set name = ?, salary = ?, departmentId = (");
            query.append("SELECT id from department where department.name =? )");
            query.append("Where id= ?");
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        try (PreparedStatement pstmt = connection.prepareStatement(query.toString());

        ) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, salary);
            pstmt.setString(3, departmentName);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
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

    public static void deleteEmployeeById(int id) {
        String query = "DELETE FROM EMPLOYEE WHERE id = ?";
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        try (PreparedStatement pstmt = connection.prepareStatement(query);

        ) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
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

    public void insertUseJPA(EmployeeEntity employeeEntity) {
        EntityManager entityManager = JPAUtils.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.persist(employeeEntity);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();

            transaction.rollback();
            throw e;
        } finally {
            entityManager.close();
        }
    }

//    public static List<EmployeeEntity> findAllEntity(){
//        EntityManager entityManager = JPAUtils.getEntityManager();
//        TypedQuery<EmployeeEntity> query = entityManager.createNamedQuery("EmployeeEntity.findAll", EmployeeEntity.class);
//        List<EmployeeEntity> employeeEntityList =new ArrayList<>();
//        employeeEntityList =query.getResultList();
//        return employeeEntityList;
//    }
}
