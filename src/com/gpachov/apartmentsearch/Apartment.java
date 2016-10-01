package com.gpachov.apartmentsearch;

public class Apartment {
    private float livingArea; //square metres
    private int floor;
    private String phone;
    private String link;
    private boolean centralStateHeating;
    private float price;
    private int year;
    private String locatedIn;

    public boolean isCentralStateHeating() {
        return centralStateHeating;
    }

    public void setCentralStateHeating(boolean centralStateHeating) {
        this.centralStateHeating = centralStateHeating;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public float getLivingArea() {
        return livingArea;
    }

    public void setLivingArea(float livingArea) {
        this.livingArea = livingArea;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getLocatedIn() {
        return locatedIn;
    }

    public void setLocatedIn(String locatedIn) {
        this.locatedIn = locatedIn;
    }

    @Override
    public String toString() {
        return "{" +
                "area=" + livingArea +
                ", floor=" + floor +
//                ", centralStateHeating=" + centralStateHeating +
                ", price=" + price +
                ", year=" + year +
                ", locatedIn='" + locatedIn + '\'' +
                ", link='" + link + '\'' +
                '}';
    }

    public enum BuildType {
        BRICK(), EPK(), OTHER();
    }
}
