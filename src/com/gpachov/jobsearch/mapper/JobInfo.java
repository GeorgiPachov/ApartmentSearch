package com.gpachov.jobsearch.mapper;

import java.util.List;

public class JobInfo {
    private List<String> technologies;
    private String salary;
    private String position;

    public JobInfo(List<String> technologies, String salary, String position, String company) {
        this.technologies = technologies;
        this.salary = salary;
        this.position = position;
        this.company = company;
    }

    public List<String> getTechnologies() {

        return technologies;
    }

    public void setTechnologies(List<String> technologies) {
        this.technologies = technologies;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    private String company;
}
