package com.gpachov.jobsearch;

import com.gpachov.CommonUtils;
import com.gpachov.jobsearch.mapper.CWJobsMapper;
import com.gpachov.jobsearch.mapper.JobInfo;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        CWJobsMapper mapper = new CWJobsMapper();
        List<JobInfo> jobInfoList = mapper.get();
        jobInfoList.forEach(System.out::println);
        CommonUtils.publishJobs(jobInfoList);
    }
}
