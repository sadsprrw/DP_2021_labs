package model;

import java.io.Serializable;

public class House implements Serializable{
    private String id;
    private Integer apartmentNumber;
    private Integer area;
    private Integer floor;
    private Integer roomsCount;
    private String street;
    private String type;
    private Integer lifetime;

    public House(String id, Integer apartmentNumber, Integer area, Integer floor, Integer roomsCount, String street,
                 String type, Integer lifetime) {
        this.id = id;
        this.apartmentNumber = apartmentNumber;
        this.area = area;
        this.floor = floor;
        this.roomsCount = roomsCount;
        this.street = street;
        this.type = type;
        this.lifetime = lifetime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(Integer apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getRoomsCount() {
        return roomsCount;
    }

    public void setRoomsCount(Integer roomsCount) {
        this.roomsCount = roomsCount;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getLifetime() {
        return lifetime;
    }

    public void setLifetime(Integer lifetime) {
        this.lifetime = lifetime;
    }


    public String toString(){
        return ""+id+" "+apartmentNumber+" "+area+" "+" "+floor+" "+roomsCount+" "+street+" "+type+" "+lifetime;
    }
}