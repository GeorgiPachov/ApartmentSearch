package com.gpachov.jobsearch.mapper;

import com.gpachov.apartmentsearch.mapper.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jore on 1/28/2017.
 */
public class JobsBgJobMapper implements JobMapper {

    private static final String JOBS_BG_TEAM_LEAD_SEARCH = "https://www.jobs.bg/front_job_search.php?first_search=1&distance=0&location_sid=&all_categories=0&all_type=0&position_level%5B%5D=1&position_level%5B%5D=3&keywords%5B%5D=Java&keyword=";

    @Override
    public String getURL() {
        return JOBS_BG_TEAM_LEAD_SEARCH;
    }

    @Override
    public List<JobInfo> get() {
        try {
            String content = Utils.getContentUtf8(getURL());
            System.out.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
