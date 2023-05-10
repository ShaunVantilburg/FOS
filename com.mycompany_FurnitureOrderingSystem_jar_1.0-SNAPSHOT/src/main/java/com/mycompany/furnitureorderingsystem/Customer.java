package com.mycompany.furnitureorderingsystem;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.util.Date;

/**
 *
 * @author Shaun
 */
public class Customer {

    public final String name;
    private final Date dob;
    private final String address;
    public Customer(String name, Date dob, String address){
        this.name = name;
        this.dob = dob;
        this.address = address;
    }
    @Override
    public String toString() {
        return "Name: " + name +
                " DOB: " + dob +
                " Address: " + address;
    }
}
