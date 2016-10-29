package com.gpachov.apartmentsearch.mapper;

import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by georgi.pachov on 02/10/2016.
 */
public class Utils {

    public static String regexSearch(String regex, int group, String content) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String locatedIn = matcher.group(group);
            return locatedIn.trim().toLowerCase();
        }
        return "unknown";
    }


    public static String getContent(String link) throws IOException {
        return Request.Get(link).execute().returnContent().asString(Charset.forName("Windows-1251"));
    }

    public static String getContentUtf8(String link) throws IOException {
        return Request.Get(link).execute().returnContent().asString();
    }

}
