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
    private boolean isLastFloor;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Apartment apartment = (Apartment) o;

        if (Float.compare(apartment.livingArea, livingArea) != 0) return false;
        if (floor != apartment.floor) return false;
        if (centralStateHeating != apartment.centralStateHeating) return false;
        if (Float.compare(apartment.price, price) != 0) return false;
        if (year != apartment.year) return false;
        if (phone != null ? !phone.equals(apartment.phone) : apartment.phone != null) return false;
        if (link != null ? !link.equals(apartment.link) : apartment.link != null) return false;
        return locatedIn != null ? locatedIn.equals(apartment.locatedIn) : apartment.locatedIn == null;

    }

    @Override
    public int hashCode() {
        int result = (livingArea != +0.0f ? Float.floatToIntBits(livingArea) : 0);
        result = 31 * result + floor;
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (link != null ? link.hashCode() : 0);
        result = 31 * result + (centralStateHeating ? 1 : 0);
        result = 31 * result + (price != +0.0f ? Float.floatToIntBits(price) : 0);
        result = 31 * result + year;
        result = 31 * result + (locatedIn != null ? locatedIn.hashCode() : 0);
        return result;
    }

    public void setIsLastFloor(boolean isLastFloor) {
        this.isLastFloor = isLastFloor;
    }

    public boolean isLastFloor() {
        return isLastFloor;
    }


    public enum BuildType {
        BRICK(), EPK(), OTHER();
    }
}
