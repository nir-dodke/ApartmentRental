package com.rental.service.bean;

public class ApartmentUnit {
    String aptNo;
    String renterName;
    boolean isRented;

    public ApartmentUnit(String aptNo, String renterName, boolean isRented) {
        this.aptNo = aptNo;
        this.renterName = renterName;
        this.isRented = isRented;
    }

    public String getAptNo() {
        return aptNo;
    }

    public void setAptNo(String aptNo) {
        this.aptNo = aptNo;
    }

    public String getRenterName() {
        return renterName;
    }

    public void setRenterName(String renterName) {
        this.renterName = renterName;
    }

    public boolean isRented() {
        return isRented;
    }

    public void setRented(boolean rented) {
        isRented = rented;
    }

    @Override
    public int hashCode() {
        return this.aptNo.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        ApartmentUnit unit = obj instanceof ApartmentUnit ? ((ApartmentUnit) obj) : null;
        return unit != null && this.aptNo.equals(unit.getAptNo());
    }
}
