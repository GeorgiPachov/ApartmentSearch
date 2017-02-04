package com.gpachov.jobsearch;

import com.gpachov.jobsearch.mapper.JobInfo;
import com.gpachov.jobsearch.mapper.JobsBgJobMapper;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        JobsBgJobMapper mapper = new JobsBgJobMapper();
        List<JobInfo> jobInfoList = mapper.get();
        jobInfoList.forEach(System.out::println);
    }
}
