package com.manhlee.controller;

import com.manhlee.dao.DepartmentDao;
import com.manhlee.dao.EmployeeDao;
import com.manhlee.model.Department;
import com.manhlee.model.Employee;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "EditServlet", value = "/edit")
public class EditServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Employee employee = new Employee();
        employee= EmployeeDao.findEmployee(id);
        request.setAttribute("employee", employee);
        List<Department> departmentList = new ArrayList<>();
        departmentList = DepartmentDao.findAllDepartment();
        request.setAttribute("departmentList",departmentList);
        request.getRequestDispatcher("/editpage.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String salaryStr = request.getParameter("salary");
        double salary = 0;
        if(salaryStr!=null && salaryStr.length()>0){
            salary= Double.parseDouble(salaryStr);
        }
        String departmentName = request.getParameter("departmentName");;
        EmployeeDao.editEmployee(id,name,salary,departmentName);
        response.sendRedirect("load");
    }
}
