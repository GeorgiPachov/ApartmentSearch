package com.gpachov.jobsearch.mapper;

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
 * Created by Jore on 8/29/2017.
 */
public class CWJobsMapper extends BaseJobMapper {
    @Override
    public String getURL() {
        return "https://www.cwjobs.co.uk/jobs/java-contract?Sort=4";
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

    protected List<String> getLinks(String content) {
//        <a href="/job/java-developer/digital-skills-job75664854?entryurl=%2fjobs%2fjava-contract%3fsort%3d4%2375664854" title="See details for a Java developer in test in CR0 (matches on java developer)">
//                    <h2>Java developer in test</h2>
//                </a>
        Pattern pattern = Pattern.compile("href=\"(/job/\\S{1,120})\"");
        Matcher matcher = pattern.matcher(content);
        Set<String> links = new HashSet<>();
        while (matcher.find()) {
            links.add("http://www.cwjobs.co.uk" + matcher.group(1));
        }
        return new ArrayList(links);
    }

    protected String findSalary(String content) {

//        <li class="salary icon">
//            <div>Unspecified</div>
//        </li>
//        ([\w\s\-£$€]{1,32})</div>
//        return Utils.regexSearch("<li class=\"salary icon\">\n" +
//                "\\s{1,15}<div>([\\w\\s\\d£/\\.+-]{1,64})</div>\n" +
//                "\\s{1,15}</li>", 1, content);
        return Utils.regexSearch("<li class=\"salary icon\">\n" +
                "\\s{1,15}<div>(.{1,64})</div>\n", 1, content);
    }

    protected String findCompany(String content) {
        return null;
    }

    protected String findPosition(String content) {
//        <h1 class="">
//                Java developer in test
//                </h1>
        return Utils.regexSearch("<h1 class=\"\">([\\w\\s-/–(),\\.]{1,100})</h1>", 1, content);
    }
}
