package com.manhlee.dao;

import com.manhlee.helper.ConnectionPool;
import com.manhlee.model.Department;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDao {

    public static List<Department> findAllDepartment(){
        List<Department> departmentList = new ArrayList<>();
        String query= "SELECT * FROM DEPARTMENT";
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet resultSet = pstmt.executeQuery();){
            while (resultSet.next()){
                Department department = new Department();
                department.setId(resultSet.getInt("id"));
                department.setDepartmentName(resultSet.getString("name"));
                departmentList.add(department);
            }
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
        return departmentList;
    }
}
