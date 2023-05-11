package com.mycompany.furnitureorderingsystem;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Shaun
 */
public class StorageCabinet extends Furniture {
    private static final String FURNITURE = "storage cabinet";
    public int drawers;
    public StorageCabinet(String materialType, String color, double cost, double l, double w, double h, int drawers) {
        super(FURNITURE, materialType, color, cost, l, w, h);
        this.drawers = drawers;
    }
    
    @Override
    public String toString() {
        return super.toString() + "Number of drawers: " + drawers;
    }
}