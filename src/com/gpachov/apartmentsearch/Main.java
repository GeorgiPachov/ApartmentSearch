package com.gpachov.apartmentsearch;

import com.gpachov.CommonUtils;
import com.gpachov.apartmentsearch.mapper.ApartmentMapper;
import com.gpachov.apartmentsearch.mapper.HomesBGMapper;
import com.gpachov.apartmentsearch.mapper.ImotBGMapper;
import com.gpachov.apartmentsearch.mapper.OlxBGMapper;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by georgi.pachov on 27/09/2016.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        List<ApartmentMapper> mappers = new ArrayList<>();
        ImotBGMapper imotBgMapper = null;
        OlxBGMapper olxBGMapper = null;
        HomesBGMapper homesBGMapper = null;
        if (args.length > 0){
            imotBgMapper = new ImotBGMapper(args[0]);
            olxBGMapper = new OlxBGMapper(args[1]);
            homesBGMapper = new HomesBGMapper(args[2]);
        } else {
            imotBgMapper = new ImotBGMapper();
            olxBGMapper = new OlxBGMapper();
            homesBGMapper = new HomesBGMapper();
        }
        mappers.add(imotBgMapper);
        mappers.add(olxBGMapper);
        mappers.add(homesBGMapper);

        List<ApartmentInfo> infoFromAllModules = mappers.stream().map(m -> m.get()).flatMap(l -> l.stream()).sorted(Comparator.comparingDouble(apartmentInfo -> apartmentInfo.getFormulaScore() * -1)).collect(Collectors.toList());

        infoFromAllModules.forEach(System.out::println);

        CommonUtils.publishApartments(infoFromAllModules);
    }



}
