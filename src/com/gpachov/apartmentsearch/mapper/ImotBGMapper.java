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

public class ImotBGMapper implements ApartmentMapper {

    private static final String ONE_ROOM_ONLY_SEARCH_EVERYWHERE = "http://www.imot.bg/2cg82f";
    private static final String ONE_ROOM_INCLUDED = "http://www.imot.bg/2cg7sd";
    private static final String WOULD_LIVE_IN = "http://www.imot.bg/2czox6";
    private static final String IN_MY_FINANCIAL_RANGE = "http://www.imot.bg/2f05tf";
    private static final String ALL = "http://www.imot.bg/2dr707";
    private final String searchTerm;

    private Set<String> visitedLinks = new HashSet<>();

    public ImotBGMapper(String arg) {
        this.searchTerm = arg;
    }

    public ImotBGMapper() {
        this.searchTerm = IN_MY_FINANCIAL_RANGE;
    }

    public static void main(String[] args) throws IOException {
        ImotBGMapper mapper = new ImotBGMapper();
        List<ApartmentInfo> sortedInfos = mapper.get();
        sortedInfos.forEach(System.out::println);
    }

    @Override
    public List<ApartmentInfo> get() {
        String result = null;
        try {
            result = Request.Get(searchTerm)
                    .execute().returnContent().asString();
            List<String> pagesLinks = getPagesLinks(result);
            System.out.print(pagesLinks.size());
            System.out.print(pagesLinks);
            List<ApartmentInfo> infos = getApartmentInfos(result);
            for (String pageLink : pagesLinks) {
                String content = Request.Get(pageLink)
                        .execute().returnContent().asString();
                infos.addAll(getApartmentInfos(content));
            }

            List<ApartmentInfo> sortedInfos = infos.stream().sorted(Comparator.comparingDouble(apartmentInfo -> apartmentInfo.getFormulaScore() * -1)).collect(Collectors.toList());
            return sortedInfos;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<ApartmentInfo> getApartmentInfos(String result) {
        List<ApartmentInfo> infos = new ArrayList<>();
        Set<String> links = getLinks(result);
        links.forEach(link -> {
            try {
                infos.add(process(link));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return infos;
    }

    private List<String> getPagesLinks(String result) {
//        http://www.imot.bg/pcgi/imot.cgi?act=3&slink=2d2i6k&f1=2
        //http://www.imot.bg/pcgi/imot.cgi?act=3&slink=2f05tf&f1=2
        //href="//www.imot.bg/pcgi/imot.cgi?act=3&slink=2f05tf&f1=2"
        Pattern pattern = Pattern.compile("www\\.imot\\.bg/pcgi/imot\\.cgi\\?act=\\d&amp;slink=[a-z0-9]*&amp;f1=\\d");
        List<String> resultList = new ArrayList<>();
        Matcher matcher = pattern.matcher(result);
        while (matcher.find()) {
            resultList.add("http://" + matcher.group(0));
        }
        return resultList;
    }

    public ApartmentInfo process(String link) throws IOException {
        String content = Request.Get(link).execute().returnContent().asString(Charset.forName("Windows-1251"));
        Apartment apartment = mapToApartment(content);
        apartment.setLink(link);

        ApartmentInfo info = new ApartmentInfo(apartment);
        return info;
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
        Pattern pattern = Pattern.compile("град София, (\\p{InCYRILLIC}{2,20}\\s*\\d{0,2}\\p{InCYRILLIC}{0,20}\\s*\\d{0,2})");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String locatedIn = matcher.group(1);
            return locatedIn.trim().toLowerCase();
        }
        return "unknown";
    }

    public int findYear(String content) {
        Pattern pattern = Pattern.compile("\\D{1,5}\\s{0,3},\\s{0,3}(\\d{4})\\s{1,}");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String year = matcher.group(1);
            Integer yearI = Integer.valueOf(year);
            return yearI;
        }

        return -1;
    }

    public int findFloor(String content) {
        Pattern pattern = Pattern.compile("Етаж:\\s{0,10}(\\d{1,2})");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String floor = matcher.group(1);
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

    @Override
    public String getURL() {
        return null;
    }

    public float findPrice(String content) {
        Pattern pattern = Pattern.compile("(\\d{4,5})\\s{0,2}EUR");
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
        String regex = "www.imot.bg/pcgi/imot.cgi\\?.*adv=.*\"??";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(result);
        while (matcher.find()) {
            if (!visitedLinks.contains(matcher.group())) {
                results.add("http://" + matcher.group().split(" ")[0].replace("\"", ""));
                visitedLinks.add(matcher.group());
            }
        }
//        href="//www.imot.bg/pcgi/imot.cgi?act=5&adv=1b145380499623468&slink=2f05tf&f1=1"
//        http://www.imot.bg/pcgi/imot.cgi?act=5&adv=1b146979309238901&slink=2cbbj1&f1=1
        return results;

    }
}
