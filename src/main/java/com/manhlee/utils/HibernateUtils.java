package com.manhlee.utils;

import com.manhlee.model.DepartmentEntity;
import com.manhlee.model.EmployeeEntity;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

public class HibernateUtils {
    private static final SessionFactory FACTORY;

    static {
        Configuration configuration = new Configuration();

        Properties properties = new Properties();
        properties.put(Environment.DIALECT,"org.hibernate.dialect.MySQLDialect");
        properties.put(Environment.DRIVER,"com.mysql.cj.jdbc.Driver");
        properties.put(Environment.URL,"jdbc:mysql://localhost:3306/jv44");
        properties.put(Environment.USER,"root");
        properties.put(Environment.PASS,"!sml123A@z");
        properties.put(Environment.SHOW_SQL,true);

        configuration.setProperties(properties);

        //add persistent class
        configuration.addAnnotatedClass(DepartmentEntity.class);
        configuration.addAnnotatedClass(EmployeeEntity.class);


        ServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();
        FACTORY=configuration.buildSessionFactory(registry);
    }

    public static SessionFactory getFactory(){
        return FACTORY;
    }
}
