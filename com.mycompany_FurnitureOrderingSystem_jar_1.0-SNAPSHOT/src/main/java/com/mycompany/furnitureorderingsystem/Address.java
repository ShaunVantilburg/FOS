package com.mycompany.furnitureorderingsystem;

public record Address(String street, String city, String state, String zip){
    @Override
    public String toString() {
        return street + ", " +
                city + ", " +
                state + " " +
                zip;
    }
}
