package com.gpachov.apartmentsearch;

import com.gpachov.apartmentsearch.mapper.ApartmentMapper;
import com.gpachov.apartmentsearch.mapper.HomesBGMapper;
import com.gpachov.apartmentsearch.mapper.ImotBGMapper;
import com.gpachov.apartmentsearch.mapper.OlxBGMapper;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collector;
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

        String resultsFile = mapToHtml(infoFromAllModules);
        String os = System.getProperty("os.name");
        String command = "";
        if (os.toLowerCase().contains("windows")) {
            command = "firefox ";
        } else if (os.toLowerCase().contains("linux")) {
            command = "firefox ";
        } else {
            command = "open ";
        }
        Runtime.getRuntime().exec(command + resultsFile);
    }
    public static String mapToHtml(List<ApartmentInfo> infoFromAllModules, String resFile) throws IOException {
        VelocityEngine ve = new VelocityEngine();
        ve.init();

        String header = "<html><head></head> <body>" +
                "<table>" + "<tr><td>Nomer </td> <td> Rezultat </td> <td> Jilishtna Plosht (kvadratni metri)</td> <td>Etaj: </td> <td> Cena (v evro)</td> <td> Godina na stroej</td> <td>Kvartal/mestnost v Sofiq</td> <td>Link kum obiavata</td> </tr>";
        StringBuilder middle = new StringBuilder();
        String footer = "   </table></body></html>";

        List<ApartmentInfo> apartments = infoFromAllModules.stream().collect(Collectors.toList());
        for (ApartmentInfo a : apartments) {
            a.getApartment().setLocatedIn(translate(a.getApartment().getLocatedIn()));
        }
//        apartments = apartments.stream().sorted((c1, c2) ->
//                -1* Float.compare(c1.getFormulaScore(), c2.getFormulaScore())).collect(Collectors.toList());
        apartments = apartments.stream().sorted((c1, c2) ->
                -1* Float.compare(c1.getLocationScore(), c2.getLocationScore())).collect(Collectors.toList());

        for (int i = 0; i < apartments.size(); i++) {
            ApartmentInfo info = apartments.get(i);
            Apartment a = apartments.get(i).getApartment();
            middle.append(
                    "<tr><td>" + i + "</td><td>" + info.getFormulaScore() + "</td>" + "<td>" + a.getLivingArea() + "kv.m.</td> " +
                    "<td>" + a.getFloor() +" </td> <td> " + a.getPrice() + "EUR</td> <td> " + a.getYear() + "</td>" +
                    "<td>" + a.getLocatedIn() +"</td> <td><a href=\"" + a.getLink() + "\" target=\"_blank\">" + a.getLink() +"</td> </tr> ");

        }
        Files.delete(Paths.get(resFile));
        FileWriter fileWriter = new FileWriter(Paths.get(resFile).toAbsolutePath().toString());
        fileWriter.write(header + middle + footer);
        fileWriter.flush();
        return resFile;
    }
    private static String mapToHtml(List<ApartmentInfo> infoFromAllModules) throws IOException {
        String resultFile = "apartment-report.html";
        return mapToHtml(infoFromAllModules, resultFile);
    }

    private static String translate(String inBulgarian) {
        StringBuffer buffer = new StringBuffer();
        for (char c : inBulgarian.toCharArray()) {
            buffer.append(translate(c));
        }
        return buffer.toString();
    }

    private static char[] translate(char c) {
        Map<Character, String> translatations = new HashMap<>();
        translatations.put('а',"a");
        translatations.put('б',"b");
        translatations.put('в',"v");
        translatations.put('г',"g");
        translatations.put('д',"d");
        translatations.put('е',"e");
        translatations.put('ж',"j");
        translatations.put('з',"z");
        translatations.put('и',"i");
        translatations.put('й',"y");
        translatations.put('к',"k");
        translatations.put('л',"l");
        translatations.put('м',"m");
        translatations.put('н',"n");
        translatations.put('о',"o");
        translatations.put('п',"p");
        translatations.put('р',"r");
        translatations.put('с',"s");
        translatations.put('т',"t");
        translatations.put('ф',"f");
        translatations.put('у',"u");
        translatations.put('х',"h");
        translatations.put('ц',"c");
        translatations.put('ч',"ch");
        translatations.put('с',"s");
        translatations.put('ш',"sh");
        translatations.put('щ',"sht");
        translatations.put('ъ',"a");
        translatations.put('ю',"iu");
        translatations.put('я',"ia");


        String translation = translatations.get(c);
        if (translation != null) {
            return translation.toCharArray();
        } else return " ".toCharArray();
    }
}
