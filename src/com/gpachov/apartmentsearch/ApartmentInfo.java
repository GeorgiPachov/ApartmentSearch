package com.gpachov.apartmentsearch;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by georgi.pachov on 10/09/2016.
 */
public class ApartmentInfo {
    private static final float YEAR_START = 1990;
    private static final float YEAR_END = 2016;
    private static final float PRICE_PER_SQM_MIN = 600; //EUR per sqm
    private static final float PRICE_PER_SQM_MAX = 1250; //EUR per sqm
    private static final float PRICE_MIN = 25000;
    private static final float PRICE_MAX = 65000;
    private static Map<String, Float> locationScores = new HashMap<>();

    {
        {
            locationScores.put("студентски град", 0.55f);
            locationScores.put("манастирски ливади", 0.6f);
            locationScores.put("малинова долина", 0.55f);
            locationScores.put("лозенец", 1.0f);
            locationScores.put("дианабад", 1.0f);
            locationScores.put("редута", 0.65f);
            locationScores.put("дружба", 0.6f);
            locationScores.put("дружба 1", 0.6f);
            locationScores.put("оборище", 0.8f);
            locationScores.put("слатина", 0.7f);
            locationScores.put("витоша", 0.85f);
            locationScores.put("мусагеница", 0.8f);
            locationScores.put("дървеница", 0.7f);
            locationScores.put("младост 1", 0.65f);
            locationScores.put("младост 2", 0.6f);
            locationScores.put("полигона", 0.6f);
            locationScores.put("гео милев", 0.7f);
            locationScores.put("изток", 1.0f);
            locationScores.put("изгрев", 1.0f);
            locationScores.put("яворов", 0.85f);
            locationScores.put("подуяне", 0.6f);
            locationScores.put("оборище", 0.9f);
            locationScores.put("докторски паметник", 0.85f);
            locationScores.put("градина", 0.85f);
            locationScores.put("стрелбище", 0.75f);
            locationScores.put("гоце делчев", 0.65f);
            locationScores.put("хладилника", 0.85f);
            locationScores.put("ж.гр.южен парк", 0.85f);
            locationScores.put("пз хладилника", 0.75f);
            locationScores.put("кръстова вада", 0.55f);
            locationScores.put("unknown", 0.5f);
            locationScores.put("надежда 2", 0.5f);
            locationScores.put("люлин 6", 0.4f);
            locationScores.put("център", 0.8f);
            locationScores.put("славия", 0.5f);
            locationScores.put("княжево", 0.3f);
            locationScores.put("люлин 9", 0.4f);
            locationScores.put("овча купел", 0.5f);
            locationScores.put("бъкстон", 0.4f);
            locationScores.put("света троица", 0.55f);
            locationScores.put("банишора", 0.85f);
            locationScores.put("люлин", 0.4f);
            locationScores.put("бояна", 0.75f);
            locationScores.put("люлин 10", 0.3f);
            locationScores.put("овча купел 1",locationScores.get("овча купел"));
            locationScores.put("драгалевци", 0.7f);
            locationScores.put("орландовци", 0.3f);
            locationScores.put("павлово", 0.45f);
            locationScores.put("разсадника", 0.75f);
            locationScores.put("белите брези", 0.45f);
            locationScores.put("карпузица", 0.3f);
            locationScores.put("западен парк", 0.7f);
            locationScores.put("модерно предградие", 0.45f);
            locationScores.put("симеоново", 0.3f);
            locationScores.put("свобода", 0.45f);
            locationScores.put("световрачене", 0.2f);
            locationScores.put("толстой", 0.8f);
            locationScores.put("хаджи димитър", 0.75f);
            locationScores.put("левски", 0.5f);
            locationScores.put("левски г", 0.5f);
            locationScores.put("левски в", 0.5f);
            locationScores.put("зона б", 0.7f);
            locationScores.put("люлин 1", 0.35f);
            locationScores.put("люлин 7", 0.35f);
            locationScores.put("люлин 8", 0.35f);
            locationScores.put("овча купел 2", 0.45f);
            locationScores.put("световрачане", 0.1f);
            locationScores.put("дружба 2", 0.575f);
            locationScores.put("надежда 1", 0.4f);
            locationScores.put("надежда 3", 0.4f);
        }
    }

    private Apartment apartment;
    private float pricePerSqM;
    private float floorCoefficient;
    private float yearCoefficient;
    private float formulaScore;
    private float pricePerSqmNormalized;
    private float priceCoefficient;
    private float locationScore;


    public ApartmentInfo(Apartment apartment) {
        this.apartment = apartment;
        this.pricePerSqM = apartment.getPrice() / apartment.getLivingArea();

        float pricePerSqmNormalized = ((PRICE_PER_SQM_MAX - pricePerSqM) / ((float) PRICE_PER_SQM_MAX - PRICE_PER_SQM_MIN));
        setPricePerSqmNormalized(pricePerSqmNormalized);

        setPriceCoefficient((PRICE_MAX - apartment.getPrice()) / (PRICE_MAX - PRICE_MIN));
        float floorCoefficient = 1;
        if (apartment.getFloor() < 2) {
            floorCoefficient = 0.25f;
        } else if (apartment.getFloor() > 6) {
            floorCoefficient = 0.2f;
        }
        setFloorCoefficient(floorCoefficient);

        float yearCoefficient = 1 - ((YEAR_END - apartment.getYear()) / ((float) YEAR_END - YEAR_START));
        setYearCoefficient(yearCoefficient);

        Float locationScore = locationScores.get(apartment.getLocatedIn());
        if (locationScore == null) {
            System.out.println(apartment.getLocatedIn());
            setLocationScore(1f); //fallback
        } else {
            setLocationScore(locationScore);
        }
        float formulaScore = (pricePerSqmNormalized + floorCoefficient + yearCoefficient + priceCoefficient*5 + this.locationScore) / 2*5.0f;

        setFormulaScore(formulaScore);

    }

    public float getPricePerSqM() {
        return pricePerSqM;
    }

    public void setPricePerSqM(float pricePerSqM) {
        this.pricePerSqM = pricePerSqM;
    }

    public float getFloorCoefficient() {
        return floorCoefficient;
    }

    public void setFloorCoefficient(float floorCoefficient) {
        this.floorCoefficient = floorCoefficient;
    }

    public void setYearCoefficient(float yearCoefficient) {
        this.yearCoefficient = yearCoefficient;
    }

    public void setPricePerSqmNormalized(float pricePerSqmNormalized) {
        this.pricePerSqmNormalized = pricePerSqmNormalized;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        return "{" +
                "score=" + df.format(formulaScore) +
                ", price=" + df.format(priceCoefficient) +
                ", location=" + df.format(locationScore) +
                ", floor=" + df.format(floorCoefficient) +
                ", yearC=" + df.format(yearCoefficient) +
                ", pricePerM=" + df.format(pricePerSqmNormalized) +
                ", apartment=" + apartment +
                '}';
    }

    public void setFormulaScore(float formulaScore) {
        this.formulaScore = formulaScore;
    }

    public float getFormulaScore() {
        return formulaScore;
    }

    public float getPriceCoefficient() {
        return priceCoefficient;
    }

    public void setPriceCoefficient(float priceCoefficient) {
        this.priceCoefficient = priceCoefficient;
    }

    public float getLocationScore() {
        return locationScore;
    }

    public void setLocationScore(float locationScore) {
        this.locationScore = locationScore;
    }
}
