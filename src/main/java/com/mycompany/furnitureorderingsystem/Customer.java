package com.mycompany.furnitureorderingsystem;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Shaun
 */
public class Customer {

    // For GUI testing
    public final String name;
    public Customer(String name){
        this.name = name;
    }
    // For GUI testing
    @Override
    public String toString() {
        return name;
    }
}
