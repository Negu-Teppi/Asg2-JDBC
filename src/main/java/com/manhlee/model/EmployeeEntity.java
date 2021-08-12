package com.manhlee.model;

import javax.persistence.*;
import java.text.NumberFormat;

@Entity
@Table(name = "employee")
public class EmployeeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private double salary;

    @ManyToOne()
    @JoinColumn(name = "departmentId")
    private DepartmentEntity department;

    public EmployeeEntity() {
    }

    public EmployeeEntity(int id, String name, double salary, DepartmentEntity department) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.department = department;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public DepartmentEntity getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentEntity department) {
        this.department = department;
    }
}
