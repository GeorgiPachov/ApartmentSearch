package com.gpachov.jobsearch.mapper;

import com.gpachov.Mapper;

import java.util.List;

public interface JobMapper extends Mapper{
    List<JobInfo> get();
}
