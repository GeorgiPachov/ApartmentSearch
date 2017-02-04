package com.gpachov.apartmentsearch.mapper;

import com.gpachov.apartmentsearch.Apartment;
import com.gpachov.apartmentsearch.ApartmentInfo;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by georgi.pachov on 01/10/2016.
 */
public class YavlenaMapper implements ApartmentMapper {

    private final String ALL = "http://www.yavlena.com/imoti/prodajbi/%D0%A1%D0%BE%D1%84%D0%B8%D1%8F%20%28%D1%81%D1%82%D0%BE%D0%BB%D0%B8%D1%86%D0%B0%29/%D0%A1%D0%BE%D1%84%D0%B8%D1%8F/?ptype=TwoRoomsAppartment,ThreeRoomsAppartment&pto=65000&ctype=Brick&view=List";

    @Override
    public String getURL() {
        return ALL;
    }

    @Override
    public String getUnfurnituredPanel() {
        return null;
    }

    @Override
    public String getFurnituredUrl() {
        return null;
    }

    @Override
    public String getUnfurnituredUrl() {
        return null;
    }

    @Override
    public float findPrice(String content) {
        return 0;
    }

    @Override
    public ApartmentInfo process(String link) throws IOException {
        return null;
    }

    @Override
    public Apartment mapToApartment(String content) {
        return null;
    }

    @Override
    public boolean findIsLastFloor(String content) {
        return false;
    }

    @Override
    public String findLocatedIn(String content) {
        return null;
    }

    @Override
    public int findYear(String content) {
        return 0;
    }

    @Override
    public int findFloor(String content) {
        return 0;
    }

    @Override
    public float findLivingArea(String content) {
        return 0;
    }

    @Override
    public Set<String> getLinks(String result) {
        return new HashSet<>();
    }
}
