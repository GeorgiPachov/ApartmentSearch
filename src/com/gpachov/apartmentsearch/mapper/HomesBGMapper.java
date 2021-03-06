package com.gpachov.apartmentsearch.mapper;

import com.gpachov.CommonUtils;
import com.gpachov.apartmentsearch.ApartmentInfo;
import com.gpachov.apartmentsearch.Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.gpachov.apartmentsearch.mapper.Utils.regexSearch;

/**
 * Created by georgi.pachov on 01/10/2016.
 */
public class HomesBGMapper implements ApartmentMapper {
    private static final String UNFURNITURED_PANEL_NEW ="http://www.homes.bg/index.php?go=apartmentssell&search=1&advanced=&fromhomeu=2&publishedTime=0&filterOrderBy=1&showPrice=&Neighbourhoods=&morgagesells=&regiontype=1&locationId=1&offersfrom%5B1%5D=1&offersfrom%5B2%5D=1&offersfrom%5B3%5D=1&apartmenttype%5B2%5D=1&apartmenttype%5B3%5D=1&priceFrom=0&priceTo=65000&currencyId=1&areaFrom=&areaTo=&furnitureId=3&heatingId=0&finished=1&year_builtId=3&constructionstageId=&floorFrom=0&floorTo=0&built_typeId=";
//    private static final String FURNITURED = "http://www.homes.bg/index.php?go=apartmentssell&search=1&advanced=&fromhomeu=2&publishedTime=0&filterOrderBy=1&showPrice=&Neighbourhoods=&morgagesells=&regiontype=1&locationId=1&offersfrom%5B1%5D=1&offersfrom%5B2%5D=1&offersfrom%5B3%5D=1&apartmenttype%5B2%5D=1&apartmenttype%5B3%5D=1&priceFrom=0&priceTo=65000&currencyId=1&areaFrom=&areaTo=&furnitureId=2&heatingId=0&finished=1&year_builtId=3&constructionstageId=&floorFrom=0&floorTo=0&built_typeId=1";
    private static final String FURNITURED = "http://www.homes.bg/index.php?go=apartmentssell&search=1&advanced=&fromhomeu=1&publishedTime=0&filterOrderBy=1&showPrice=&Neighbourhoods=451%2C450%2C452%2C453%2C454%2C455%2C456%2C457%2C458%2C459%2C460&morgagesells=&regiontype=1&locationId=1&offersfrom%5B1%5D=1&offersfrom%5B2%5D=1&offersfrom%5B3%5D=1&apartmenttype%5B1%5D=1&apartmenttype%5B2%5D=1&apartmenttype%5B3%5D=1&apartmenttype%5B4%5D=1&apartmenttype%5B5%5D=1&finished=1&area=100&price=&currencyId=1";
    private static final String UNFURNITURED ="http://www.homes.bg/index.php?go=apartmentssell&search=1&advanced=&fromhomeu=2&publishedTime=0&filterOrderBy=1&showPrice=&Neighbourhoods=&morgagesells=&regiontype=1&locationId=1&offersfrom%5B1%5D=1&offersfrom%5B2%5D=1&offersfrom%5B3%5D=1&apartmenttype%5B2%5D=1&apartmenttype%5B3%5D=1&priceFrom=0&priceTo=65000&currencyId=1&areaFrom=&areaTo=&furnitureId=3&heatingId=0&finished=1&year_builtId=3&constructionstageId=&floorFrom=0&floorTo=0&built_typeId=1";
    private String link;
    private Set<String> visitedLinks = new HashSet<>();

    public HomesBGMapper(String arg) {
        this.link = arg;
    }

    public HomesBGMapper() {

    }

    public static void main(String[] args) throws IOException {
        HomesBGMapper mapper = new HomesBGMapper();
        List<ApartmentInfo> sortedInfos = mapper.get();
        CommonUtils.mapApartmentsToHtml(sortedInfos, HomesBGMapper.class.getSimpleName() + ".html");
        sortedInfos.forEach(System.out::println);
    }

    @Override
    public String getUnfurnituredPanel() {
        return UNFURNITURED_PANEL_NEW;
    }

    @Override
    public String getFurnituredUrl() {
        return FURNITURED;
    }

    @Override
    public String getUnfurnituredUrl() {
        return UNFURNITURED;
    }

    @Override
    public float findPrice(String content) {
        return toFloat(regexSearch("<strong>(\\d{1,3},\\d{1,3}) </strong></span> EUR", 1, content).replaceAll(",",""));
    }

    @Override
    public boolean findIsLastFloor(String content) {
        return false;
    }

    @Override
    public String findLocatedIn(String content) {
        return regexSearch("<b>(кв|жк). (\\p{InCyrillic}{2,20}\\s{0,2}\\p{InCyrillic}{0,20}\\s{0,2}\\d{0,2}), София</b>\n", 2, content);
    }

    @Override
    public int findYear(String content) {
        return toInt(regexSearch("Построен .* (\\d{4})г.", 1, content));
    }

    @Override
    public int findFloor(String content) {
        return toInt(regexSearch("Етаж (\\d) от \\d\n", 1, content));
    }

    @Override
    public float findLivingArea(String content) {
        return toFloat(regexSearch("(\\d{1,3})m<sup>2</sup>", 1, content));
    }

    @Override
    public Set<String> getLinks(String result) {
        Pattern pattern = Pattern.compile("as\\d{4,8}");
        Matcher matcher = pattern.matcher(result);
        Set<String> links = new HashSet<>();
        while (matcher.find()) {
            if (!visitedLinks.contains(matcher.group())) {
                links.add("http://www.homes.bg/" + matcher.group(0));
                visitedLinks.add(matcher.group());
            }
        }
        System.out.print(links.size());
        return links;
    }

    @Override
    public List<ApartmentInfo> get() {
        try {
            Set<String> pages = getPageLinks(Utils.getContentUtf8(getURL()));
            List<ApartmentInfo> results = new ArrayList<>();
            for (String pageLink : pages) {
                results.addAll(ApartmentMapper.super.doGet(pageLink));
            }
            return results;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private Set<String> getPageLinks(String contentUtf8) {
        String regex = "go=apartmentssell&search=1&advanced=&fromhomeu\\S*?page=\\d{1,4}&cref=\\d{1,4}";
        Matcher matcher = Pattern.compile(regex).matcher(contentUtf8);
        Set<String> pageLinks = new HashSet<>();
        while (matcher.find()) {
            pageLinks.add("http://homes.bg/index.php?" + matcher.group());
            System.out.println("Adding " + "http://homes.bg/index.php?" + matcher.group());

        }
        System.out.println("Found " + pageLinks.size());
        return pageLinks;
    }

    @Override
    public String getURL() {
        if (link != null) {
            return link;
        }
        return ApartmentMapper.super.getURL();
    }
}
