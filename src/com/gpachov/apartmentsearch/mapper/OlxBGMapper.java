package com.gpachov.apartmentsearch.mapper;

import com.gpachov.apartmentsearch.Apartment;
import com.gpachov.apartmentsearch.ApartmentInfo;
import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by georgi.pachov on 27/09/2016.
 */
public class OlxBGMapper implements ApartmentMapper{

    private static final String WOULD_LIVE_IN = "https://www.olx.bg/nedvizhimi-imoti/prodazhbi/apartamenti/oblast-sofiya-grad/?search[filter_float_price%3Ato]=65000&search[filter_enum_atype][0]=2&search[filter_enum_atype][1]=3&search[filter_float_space%3Afrom]=40&search[filter_float_space%3Ato]=100&search[filter_float_cyear%3Afrom]=1990&search[filter_enum_ctype][0]=tuhla&search[filter_enum_nlf][0]=1&search[filter_enum_furn][0]=poluobzaveden&search[filter_enum_cstate][0]=2&search[photos]=1&search[description]=1";

    public ApartmentInfo process(String link) throws IOException {
        String content = getContentUtf8(link);
        Apartment apartment = mapToApartment(content);
        apartment.setLink(link);

        ApartmentInfo info = new ApartmentInfo(apartment);
        return info;
    }

    private String getContent(String link) throws IOException {
        return Request.Get(link).execute().returnContent().asString(Charset.forName("Windows-1251"));
    }

    private String getContentUtf8(String link) throws IOException {
        return Request.Get(link).execute().returnContent().asString();
    }

    public Apartment mapToApartment(String content) {
        Apartment apartment = new Apartment();
        float livingArea = findLivingArea(content);
        apartment.setLivingArea(livingArea);

        float price = findPrice(content);
        apartment.setPrice(price);

        int floor = findFloor(content);
        apartment.setFloor(floor);

        int year = findYear(content);
        apartment.setYear(year);

        String locatedIn = findLocatedIn(content);
        apartment.setLocatedIn(locatedIn);

        return apartment;
    }

    public String findLocatedIn(String content) {
//        "Овча купел 1, гр. София, Област София-град";
        Pattern pattern = Pattern.compile("([\\p{InCYRILLIC}\\s\\d]{3,32}),\\s\\p{InCYRILLIC}{2}\\.\\s\\p{InCYRILLIC}{5}[\\s\\.,]");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String locatedIn = matcher.group(1);
            return locatedIn.trim().toLowerCase();
        }
        return "unknown";
    }

    public static void main(String[] args) throws IOException {
        OlxBGMapper mapper = new OlxBGMapper();
//        String content = mapper.getContentUtf8("https://www.olx.bg/ad/slaviya-dvustaen-tuhla-59000e-ID6bO14.html#95f961f1c6");
//        int floor = mapper.findFloor(content);
//        System.out.println(floor);
        List<ApartmentInfo> sortedInfos = mapper.get();
        sortedInfos.forEach(System.out::println);
    }

    public int findYear(String content) {
        Pattern pattern = Pattern.compile("(\\d{4})\\s{0,3}\\p{InCYRILLIC}{1}\\s{0,3}\\.");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String year = matcher.group(1);
            Integer yearI = Integer.valueOf(year);
            return yearI;
        }

        return -1;
    }

    public int findFloor(String content) {
        Pattern pattern = Pattern.compile("\\d-\\p{InCYRILLIC}{2}");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String floor = matcher.group(0).replaceAll("\\D", "");
            Integer floorF = Integer.valueOf(floor);
            return floorF;
        }

        Pattern pattern2 = Pattern.compile("[Е|е]т.{0,10}(\\d{1,2})");
        Matcher matcher2 = pattern2.matcher(content);
        while (matcher2.find()) {
            String floor = matcher2.group(1);
            Integer floorF = Integer.valueOf(floor);
            return floorF;
        }

        return -1;
    }

    public float findPrice(String content) {
        Pattern pattern = Pattern.compile("(\\d{4,5})\\s{0,2}€");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String price = matcher.group(1);
            float priceF = Float.valueOf(price);
            return priceF;
        }
        return -1.0f;
    }

    public float findLivingArea(String content) {
        Pattern pattern = Pattern.compile("(\\d{2,3})\\s{0,10}кв\\.м");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String livingArea = matcher.group(1);
            float livingAreaF = Float.valueOf(livingArea);
            return livingAreaF;
        }
        return -1.0f;
    }

    public Set<String> getLinks(String result) {
        Set<String> results = new LinkedHashSet<>();
        String regex = "https://www.olx.bg/ad/.{0,256}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(result);
        while (matcher.find()) {
            results.add(matcher.group().split(" ")[0].replace("\"", ""));
        }

        return results;

    }

    @Override
    public List<ApartmentInfo> get() {
        String result = null;
        try {
            result = Request.Get(WOULD_LIVE_IN)
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
