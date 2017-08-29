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
public class JobsBgJobMapper extends BaseJobMapper {

    private static final String JOBS_BG_TEAM_LEAD_SEARCH = "https://www.jobs.bg/front_job_search.php?zone_id=0&distance=0&location_sid=1&categories%5B%5D=15&all_type=0&all_position_level=1&all_company_type=1&keywords%5B%5D=Java&keyword=&last=0&email=&subscribe=1";

    @Override
    public String getURL() {
        return JOBS_BG_TEAM_LEAD_SEARCH;
    }

    protected String findSalary(String content) {
        return null;
    }

    protected String findCompany(String content) {
        return null;
    }

    protected String findPosition(String content) {
//             <td class="jobTitle" colspan="2" style="padding-top:20px;padding-bottom:30px;" align="center">Senior Java Developer</td>
//        String regex = "class=\"jobTitle\".*?>((\\w{1,20}\\s?){1,5})</td>";
//        <td class="jobTitle" style="text-align:left; padding-bottom:20px;line-height:1.3;">Java Script Development Engineer</td>
//        String regex = ">((\\w{1,20}\\s{0,2}-?){1,5})</td>";
        String regex = "class=\"jobTitle\".*?>(.*)??</";
        return Utils.regexSearch(regex, 1, content);
    }

    protected List<String> getLinks(String content) {
        //"<a href="f3600155" class="joblink" style="font-size:18px;">JavaScript Team Leader</a>";
//        href=(f[0-9]{2,9}) class="joblink"
        //<td class="jobTitle" style="text-align:left; padding-bottom:20px;line-height:1.3;">Java Script Development Engineer</td>
        Pattern pattern = Pattern.compile("href=\"(job/[0-9]{2,10})\"");
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
