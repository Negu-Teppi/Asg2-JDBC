package com.manhlee.dao;

import com.manhlee.model.DepartmentEntity;
import com.manhlee.utils.HibernateUtils;
import org.hibernate.Session;

import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

public class DepartmentEntityDao implements Serializable {

    public static void main(String[] args) {
        Session session = HibernateUtils.getFactory().openSession();

        Query query = session.createQuery("FROM DepartmentEntity ");
        List<DepartmentEntity> departments = query.getResultList();

        departments.forEach(departmentEntity -> System.out.printf("%d - %s\n", departmentEntity.getId(),
                departmentEntity.getDepartmentName()));

        session.close();
    }
}
