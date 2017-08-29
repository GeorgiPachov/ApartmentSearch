package com.gpachov.jobsearch.mapper;

import java.util.List;

public class JobInfo {
    private List<String> technologies;
    private String salary;
    private String position;

    @Override
    public String toString() {
        return "JobInfo{" +
                "technologies=" + technologies +
                ", salary='" + salary + '\'' +
                ", position='" + position + '\'' +
                ", link='" + link + '\'' +
                ", company='" + company + '\'' +
                '}';
    }

    private String link;
    private String company;

    public JobInfo() {
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


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
