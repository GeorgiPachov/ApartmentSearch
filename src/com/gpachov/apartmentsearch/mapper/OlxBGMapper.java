

package com.gpachov.apartmentsearch.mapper;

import com.gpachov.apartmentsearch.ApartmentInfo;
import com.gpachov.apartmentsearch.Main;
import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.gpachov.apartmentsearch.mapper.Utils.regexSearch;

/**
 * Created by georgi.pachov on 27/09/2016.
 */
public class OlxBGMapper implements ApartmentMapper {

    private static final String UNFURTINUTED_PANEL_NEW = "https://www.olx.bg/nedvizhimi-imoti/prodazhbi/apartamenti/oblast-sofiya-grad/?search%5Bfilter_float_price%3Ato%5D=65000&search%5Bfilter_enum_atype%5D%5B0%5D=2&search%5Bfilter_enum_atype%5D%5B1%5D=3&search%5Bfilter_float_space%3Afrom%5D=40&search%5Bfilter_float_space%3Ato%5D=100&search%5Bfilter_float_cyear%3Afrom%5D=1990&search%5Bfilter_enum_ctype%5D%5B0%5D=tuhla&search%5Bfilter_enum_ctype%5D%5B1%5D=panel&search%5Bfilter_enum_ctype%5D%5B2%5D=epk&search%5Bfilter_enum_nlf%5D%5B0%5D=1&search%5Bfilter_enum_cstate%5D%5B0%5D=2&search%5Bphotos%5D=1&search%5Bdescription%5D=1";
    private static final String FURNITURED_URL = "https://www.olx.bg/nedvizhimi-imoti/prodazhbi/apartamenti/oblast-sofiya-grad/?search[filter_float_price%3Ato]=65000&search[filter_enum_atype][0]=2&search[filter_enum_atype][1]=3&search[filter_float_space%3Afrom]=40&search[filter_float_space%3Ato]=100&search[filter_float_cyear%3Afrom]=1990&search[filter_enum_ctype][0]=tuhla&search[filter_enum_nlf][0]=1&search[filter_enum_furn][0]=poluobzaveden&search[filter_enum_cstate][0]=2&search[photos]=1&search[description]=1";
    private static final String UNFURNITURED = "https://www.olx.bg/nedvizhimi-imoti/prodazhbi/apartamenti/oblast-sofiya-grad/?search%5Bfilter_float_price%3Ato%5D=65000&search%5Bfilter_enum_atype%5D%5B0%5D=3&search%5Bfilter_enum_atype%5D%5B1%5D=2&search%5Bfilter_float_space%3Afrom%5D=40&search%5Bfilter_float_space%3Ato%5D=100&search%5Bfilter_float_cyear%3Afrom%5D=1990&search%5Bfilter_enum_ctype%5D%5B0%5D=tuhla&search%5Bfilter_enum_nlf%5D%5B0%5D=1&search%5Bfilter_enum_cstate%5D%5B0%5D=2&search%5Bphotos%5D=1&search%5Bdescription%5D=1";
    private Set<String> visitedLinks = new HashSet<>();

    public String findLocatedIn(String content) {
        String regex = "([\\p{InCYRILLIC}\\s\\d]{3,32}),\\s\\p{InCYRILLIC}{2}\\.\\s\\p{InCYRILLIC}{5}[\\s\\.,]";
        return regexSearch(regex, 1, content);
    }

    public static void main(String[] args) throws IOException {
        OlxBGMapper mapper = new OlxBGMapper();
        List<ApartmentInfo> sortedInfos = mapper.get();
        Main.mapToHtml(sortedInfos, OlxBGMapper.class
                .getSimpleName() + ".html");
        sortedInfos.forEach(System.out::println);

    }

    public int findYear(String content) {
        String regex = "(\\d{4})\\s{0,3}\\p{InCYRILLIC}{1}\\s{0,3}\\.";
        return toInt((regexSearch(regex, 1, content)));
    }

    public int findFloor(String content) {
        String regex = "\\d-\\p{InCYRILLIC}{2}";
        int result = toInt(regexSearch(regex, 0, content).replaceAll("\\D", ""));
        if (result > -1) {
            return result;
        }
        regex = "[Е|е]т.{0,10}(\\d{1,2})";
        return toInt(regexSearch(regex, 1, content).replaceAll("\\D", ""));
    }

    @Override
    public String getUnfurnituredPanel() {
        return UNFURTINUTED_PANEL_NEW;
    }

    @Override
    public String getFurnituredUrl() {
        return FURNITURED_URL;
    }

    @Override
    public String getUnfurnituredUrl() {
        return UNFURNITURED;
    }

    public float findPrice(String content) {
        String regex = "(\\d{4,5})\\s{0,2}€";
        return toFloat(regexSearch(regex, 1, content));
    }

    @Override
    public boolean findIsLastFloor(String content) {
        return false;
    }

    public float findLivingArea(String content) {
        String regex = "(\\d{2,3})\\s{0,10}кв\\.м";
        return toFloat(regexSearch(regex, 1, content));
    }

    public Set<String> getLinks(String result) {
        Set<String> results = new LinkedHashSet<>();
        String regex = "https://www.olx.bg/ad/.{0,256}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(result);
        while (matcher.find()) {
            if (!visitedLinks.contains(matcher.group())) {
                results.add(matcher.group().split(" ")[0].replace("\"", ""));
                visitedLinks.add(matcher.group());
            }
        }

        return results;

    }

    @Override
    public List<ApartmentInfo> get() {
        String result = null;
        try {
            result = Request.Get(getURL())
                    .execute().returnContent().asString();
            List<ApartmentInfo> infos = new ArrayList<>();
            Set<String> links = getLinks(result);
            links.forEach(link -> {
                try {
                    infos.add(process(link));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            List<ApartmentInfo> sortedInfos = infos.stream().sorted(Comparator.comparingDouble(apartmentInfo -> apartmentInfo.getFormulaScore() * -1)).collect(Collectors.toList());
            return sortedInfos;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


}
