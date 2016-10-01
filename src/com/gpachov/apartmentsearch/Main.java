package com.gpachov.apartmentsearch;

import com.gpachov.apartmentsearch.mapper.ApartmentMapper;
import com.gpachov.apartmentsearch.mapper.ImotBGMapper;
import com.gpachov.apartmentsearch.mapper.OlxBGMapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by georgi.pachov on 27/09/2016.
 */
public class Main {

    public static void main(String[] args) {
        List<ApartmentMapper> mappers = new ArrayList<>();
        mappers.add(new ImotBGMapper());
        mappers.add(new OlxBGMapper());

        List<ApartmentInfo> infoFromAllModules = mappers.stream().map(m -> m.get()).flatMap(l -> l.stream()).sorted(Comparator.comparingDouble(apartmentInfo -> apartmentInfo.getFormulaScore() * -1)).collect(Collectors.toList());
        infoFromAllModules.forEach(System.out::println);
        System.out.print(infoFromAllModules.size());
    }
}
