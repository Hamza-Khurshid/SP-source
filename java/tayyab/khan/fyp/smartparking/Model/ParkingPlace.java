package tayyab.khan.fyp.smartparking.Model;

import java.io.Serializable;

public class ParkingPlace implements Serializable {
    private String id, customerId, parkingPlace, parkingArea, parkingSlot, ppLat, ppLong;
    private String isBooked, isRoofed;

    public ParkingPlace() {
    }

    public ParkingPlace(String id, String customerId, String parkingPlace, String parkingArea, String parkingSlot, String ppLat, String ppLong, String isBooked, String isRoofed) {
        this.id = id;
        this.customerId = customerId;
        this.parkingPlace = parkingPlace;
        this.parkingArea = parkingArea;
        this.parkingSlot = parkingSlot;
        this.ppLat = ppLat;
        this.ppLong = ppLong;
        this.isBooked = isBooked;
        this.isRoofed = isRoofed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getParkingPlace() {
        return parkingPlace;
    }

    public void setParkingPlace(String parkingPlace) {
        this.parkingPlace = parkingPlace;
    }

    public String getParkingArea() {
        return parkingArea;
    }

    public void setParkingArea(String parkingArea) {
        this.parkingArea = parkingArea;
    }

    public String getParkingSlot() {
        return parkingSlot;
    }

    public void setParkingSlot(String parkingSlot) {
        this.parkingSlot = parkingSlot;
    }

    public String getPpLat() {
        return ppLat;
    }

    public void setPpLat(String ppLat) {
        this.ppLat = ppLat;
    }

    public String getPpLong() {
        return ppLong;
    }

    public void setPpLong(String ppLong) {
        this.ppLong = ppLong;
    }

    public String getIsBooked() {
        return isBooked;
    }

    public void setBooked(String booked) {
        isBooked = booked;
    }

    public String getIsRoofed() {
        return isRoofed;
    }

    public void setRoofed(String roofed) {
        isRoofed = roofed;
    }
}
