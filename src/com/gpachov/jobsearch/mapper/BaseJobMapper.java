package com.gpachov.jobsearch.mapper;

import com.gpachov.apartmentsearch.mapper.Utils;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Jore on 8/29/2017.
 */
public abstract class BaseJobMapper implements JobMapper{


    @Override
    public List<JobInfo> get() {
        try {
            String content = Utils.getContentUtf8(getURL());
            List<String> links = getLinks(content);
            List<JobInfo> jobInfos = links.stream().map(s -> {
                try {
                    return toJobInfo(s);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }).collect(Collectors.toList());
            return jobInfos;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    protected abstract List<String> getLinks(String content);

    protected JobInfo toJobInfo(String link) throws IOException {
        JobInfo jobInfo = new JobInfo();
        jobInfo.setLink(link);

        String content = Utils.getContentUtf8(link);
        content = StringEscapeUtils.unescapeHtml(content);
        String position = findPosition(content);
        jobInfo.setPosition(position);

        String company = findCompany(content);
        jobInfo.setCompany(company);

        String salary = findSalary(content);
        jobInfo.setSalary(salary);

        return jobInfo;
    }

    protected abstract String findSalary(String content);

    protected abstract String findCompany(String content);

    protected abstract String findPosition(String content);


}
