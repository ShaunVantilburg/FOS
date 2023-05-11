package com.mycompany.furnitureorderingsystem;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.util.Date;

/**
 * @author Shaun
 */
public record Customer(int id, String name, Date dob, Address address) {

    @Override
    public String toString() {
        return "Name: " + name +
                " DOB: " + dob +
                " Address: " + address;
    }
}
