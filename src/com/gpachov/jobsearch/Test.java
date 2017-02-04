package com.gpachov.jobsearch;

import com.gpachov.apartmentsearch.mapper.Utils;

/**
 * Created by Jore on 2/4/2017.
 */
public class Test {
    public static void main(String[] args) {
        String input = "<td class=\"jobTitle\" colspan=\"2\" style=\"padding-top:20px;padding-bottom:30px;\" align=\"center\">Senior Java Developer</td>";
        String regex = "class=\"jobTitle\".*?>((\\w{1,20}\\s?){1,5})</td>";
        System.out.println(Utils.regexSearch(regex, 1, input));

        String subInput = "Senior Java Developer";
        String subRegex = "(\\w{1,20}\\s?){1,5}";

//        System.out.println(Utils.regexSearch(subRegex, 1, subInput));

    }
}
