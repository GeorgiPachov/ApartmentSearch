package com.gpachov.apartmentsearch.mapper;

import com.gpachov.apartmentsearch.Apartment;
import com.gpachov.apartmentsearch.ApartmentInfo;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by georgi.pachov on 27/09/2016.
 */
public interface ApartmentMapper {
    float findPrice(String content);
    ApartmentInfo process(String link) throws IOException;
    Apartment mapToApartment(String content);
    String findLocatedIn(String content);
    int findYear(String content);
    int findFloor(String content);
    float findLivingArea(String content);
    Set<String> getLinks(String result);
    List<ApartmentInfo> get();
}
