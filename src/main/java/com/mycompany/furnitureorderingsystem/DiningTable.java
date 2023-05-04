package com.mycompany.furnitureorderingsystem;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Shaun
 */
public class DiningTable extends Furniture {
    private static final String FURNITURE = "dining table";
    public DiningTable(String materialType, String color, double cost, double l, double w, double h) { super(FURNITURE, materialType, color, cost, l, w, h); }
    
    public Chair[] listOfChairs;
    
    @Override
    public String toString() {
        return super.toString();
    }
}