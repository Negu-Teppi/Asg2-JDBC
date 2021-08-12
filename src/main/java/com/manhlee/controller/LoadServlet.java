package com.manhlee.controller;

import com.manhlee.dao.EmployeeDao;
import com.manhlee.model.Employee;
import com.manhlee.model.EmployeeEntity;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "LoadServlet", value = "/load")
public class LoadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        String name = request.getParameter("name");
        String salaryStr = request.getParameter("salary");
        double salary = 0;
        if(salaryStr!=null && salaryStr.length()>0){
            salary= Double.parseDouble(salaryStr);
        }
        String departmentName = request.getParameter("department");
        List<Employee> employeeList = new ArrayList<>();
        employeeList = EmployeeDao.findAllAndFindByCondition(name, salary, departmentName);
//        List<EmployeeEntity> employeeEntityList =new ArrayList<>();
//        employeeEntityList = EmployeeDao.findAllEntity();

        session.setAttribute("name",name);
        session.setAttribute("salary",salaryStr);
        session.setAttribute("departmentName",departmentName);
        request.setAttribute("employeeList", employeeList);
//        request.setAttribute("employeeList", employeeEntityList);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
