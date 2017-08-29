package com.gpachov.jobsearch.mapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jore on 8/29/2017.
 */
public class RegexTester {
    public static void main(String[] args) {
        String input = "<li class=\"salary icon\">\n" +
                "            <div>£400 - £575 per day</div>\n" +
                "        </li>";
        String regex = "<li class=\"salary icon\">\n" +
                "\\s{1,15}<div>([\\w\\s\\d£-]{1,64})</div>\n" +
                "\\s{1,15}</li>";
        Matcher matcher = Pattern.compile(regex).matcher(input);
        while (matcher.find()) {
            System.out.println(matcher.group(1));
        }
    }
}
