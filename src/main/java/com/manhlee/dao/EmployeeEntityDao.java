package com.manhlee.dao;

import com.manhlee.model.DepartmentEntity;
import com.manhlee.model.EmployeeEntity;
import com.manhlee.utils.HibernateUtils;
import org.hibernate.Session;

public class EmployeeEntityDao {

    public static void main(String[] args) {
        Session session = HibernateUtils.getFactory().openSession();

        EmployeeEntity employee = new EmployeeEntity();
        employee.setName("Minh Hang");
        employee.setSalary(16500000.0);

        DepartmentEntity department = session.get(DepartmentEntity.class,2);
        employee.setDepartment(department);

        session.save(employee);
        session.close();
    }
}
