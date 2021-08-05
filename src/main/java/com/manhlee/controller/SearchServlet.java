package com.manhlee.controller;

import com.manhlee.dao.EmployeeDao;
import com.manhlee.model.Department;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "SearchServlet", value = "/search")
public class SearchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String salaryStr = request.getParameter("salary");
        double salary = 0;
        if(salaryStr!=null && salaryStr.length()>0){
            salary= Double.parseDouble(salaryStr);
        }
        String departmentName = request.getParameter("department");
        List<? extends Department> list = new ArrayList<>();
        list = EmployeeDao.findAllAndFindByCondition(name, salary, departmentName);
        request.setAttribute("employeeList", list);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
