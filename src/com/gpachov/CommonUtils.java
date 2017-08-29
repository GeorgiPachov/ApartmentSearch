package com.gpachov;

import com.gpachov.apartmentsearch.Apartment;
import com.gpachov.apartmentsearch.ApartmentInfo;
import com.gpachov.jobsearch.mapper.JobInfo;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.velocity.app.VelocityEngine;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Jore on 8/29/2017.
 */
public class CommonUtils {
    public static String mapApartmentsToHtml(List<ApartmentInfo> infoFromAllModules) throws IOException {
        String resultFile = "apartment-report.html";
        return mapApartmentsToHtml(infoFromAllModules, resultFile);
    }


    private static String translate(String inBulgarian) {
        StringBuffer buffer = new StringBuffer();
        for (char c : inBulgarian.toCharArray()) {
            buffer.append(CommonUtils.translate(c));
        }
        return buffer.toString();
    }

    public static String mapApartmentsToHtml(List<ApartmentInfo> infoFromAllModules, String resFile) throws IOException {
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


    public static char[] translate(char c) {
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

    private static void open(String resultsFile) throws IOException {
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

    public static void publishApartments(List<ApartmentInfo> infoFromAllModules) throws IOException {
        String resultsFile = CommonUtils.mapApartmentsToHtml(infoFromAllModules);
        open(resultsFile);
    }

    public static void publishJobs(List<JobInfo> jobInfoList) throws IOException {
        String resultsFile = CommonUtils.mapJobsToHtml(jobInfoList);
        open(resultsFile);
    }

    private static String mapJobsToHtml(List<JobInfo> jobInfoList) throws IOException {
        String resFile = "job-report.html";

        VelocityEngine ve = new VelocityEngine();
        ve.init();

        String header = "<html><head></head> <body>" +
                "<table>" + "<tr><td>Nomer </td> <td> Poziciq </td> <td> Kinti</td><td>Link kum obiavata</td> </tr>";
        StringBuilder middle = new StringBuilder();
        String footer = "   </table></body></html>";
        List<JobInfo> jobInfos = jobInfoList.stream().collect(Collectors.toList());

        for (int i = 0; i < jobInfos.size(); i++) {
            JobInfo in = jobInfos.get(i);
            middle.append(
                    "<tr><td>" + i + "</td><td>" + in.getPosition() + "</td>" + "<td>" + StringEscapeUtils.escapeHtml(in.getSalary()) + "</td> " +
                            "<td><a href=\"" + in.getLink() + "\">" + in.getLink() + "</a></td>");
        }
        if (Files.exists(Paths.get(resFile))) {
            Files.delete(Paths.get(resFile));
        }
        FileWriter fileWriter = new FileWriter(Paths.get(resFile).toAbsolutePath().toString());
        fileWriter.write(header + middle + footer);
        fileWriter.flush();
        return resFile;
    }
}
