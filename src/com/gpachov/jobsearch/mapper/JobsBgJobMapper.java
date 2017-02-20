package com.gpachov.jobsearch.mapper;

import com.gpachov.apartmentsearch.Apartment;
import com.gpachov.apartmentsearch.mapper.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by Jore on 1/28/2017.
 */
public class JobsBgJobMapper implements JobMapper {

    private static final String JOBS_BG_TEAM_LEAD_SEARCH = "https://www.jobs.bg/front_job_search.php?first_search=1&distance=0&location_siМd=&all_categories=0&all_type=0&position_level%5B%5D=1&position_level%5B%5D=3&keywords%5B%5D=Java&keyword=";

    @Override
    public String getURL() {
        return JOBS_BG_TEAM_LEAD_SEARCH;
    }

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

    private JobInfo toJobInfo(String link) throws IOException {
        JobInfo jobInfo = new JobInfo();
        jobInfo.setLink(link);

        String content = Utils.getContentUtf8(link);
        String position = findPosition(content);
        jobInfo.setPosition(position);

        String company = findCompany(content);
        jobInfo.setCompany(company);

        String salary = findSalary(content);
        jobInfo.setSalary(salary);

        return jobInfo;
    }

    private String findSalary(String content) {
        return null;
    }

    private String findCompany(String content) {
        return null;
    }

    private String findPosition(String content) {
//             <td class="jobTitle" colspan="2" style="padding-top:20px;padding-bottom:30px;" align="center">Senior Java Developer</td>
//        String regex = "class=\"jobTitle\".*?>((\\w{1,20}\\s?){1,5})</td>";
        String regex = ">((\\w{1,20}\\s{0,2}-?){1,5})</td>";
        return Utils.regexSearch(regex, 1, content);
    }

    private List<String> getLinks(String content) {
        //"<a href="f3600155" class="joblink" style="font-size:18px;">JavaScript Team Leader</a>";
//        href=(f[0-9]{2,9}) class="joblink"
        Pattern pattern = Pattern.compile("href=\"(f[0-9]{2,10})\"");
        Matcher matcher = pattern.matcher(content);
        Set<String> links = new HashSet<>();
        while (matcher.find()) {
            links.add("http://www.jobs.bg/" + matcher.group(1));
        }
        System.out.print(links.size());
        System.out.println(links);
        return new ArrayList(links);
    }
}
