package com.manhlee.model;

import java.text.NumberFormat;

public class Employee extends Department{
    private int id;
    private String name;
    private double salary;

    public Employee() {
    }

    public Employee(int id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    public Employee(int id, String name1, double salary,String departmentName) {
        super(departmentName);
        this.id = id;
        this.name = name1;
        this.salary = salary;
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

    public String getSalaryFormatted(){
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        return currency.format(salary);
    }
}
