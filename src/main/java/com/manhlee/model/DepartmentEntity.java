package com.manhlee.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "department")
public class DepartmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name", nullable = false)
    private String departmentName;
    @OneToMany(mappedBy = "department")
    private List<EmployeeEntity> employeeList;
    public DepartmentEntity() {
    }

    public DepartmentEntity(int id, String departmentName) {
        this.id = id;
        this.departmentName = departmentName;
    }

    public DepartmentEntity(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public List<EmployeeEntity> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<EmployeeEntity> employeeList) {
        this.employeeList = employeeList;
    }
}
