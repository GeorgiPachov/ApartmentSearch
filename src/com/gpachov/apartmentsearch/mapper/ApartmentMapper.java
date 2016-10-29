package com.gpachov.apartmentsearch.mapper;

import com.gpachov.apartmentsearch.Apartment;
import com.gpachov.apartmentsearch.ApartmentInfo;
import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.gpachov.apartmentsearch.mapper.Utils.getContentUtf8;

/**
 * Created by georgi.pachov on 27/09/2016.
 */
public interface ApartmentMapper {
    String getURL();
    float findPrice(String content);
    default ApartmentInfo process(String link) throws IOException {
        String content = getContentUtf8(link);
        Apartment apartment = mapToApartment(content);
        apartment.setLink(link);

        ApartmentInfo info = new ApartmentInfo(apartment);
        return info;
    }
    default Apartment mapToApartment(String content) {
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
    String findLocatedIn(String content);
    int findYear(String content);
    int findFloor(String content);
    float findLivingArea(String content);
    Set<String> getLinks(String result);
    default List<ApartmentInfo> get() {
        return doGet(getURL());
    }

    default List<ApartmentInfo> doGet(String url) {
        String result = null;
        try {
            result = Request.Get(url)
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

    default float toFloat(String s) {
        if ("unknown".equals(s)) {
            return -1.0f;
        }
        return Float.valueOf(s);
    }

    default int toInt(String s) {
        if ("unknown".equals(s)) {
            return -1;
        }
        return Integer.valueOf(s);
    }
}
