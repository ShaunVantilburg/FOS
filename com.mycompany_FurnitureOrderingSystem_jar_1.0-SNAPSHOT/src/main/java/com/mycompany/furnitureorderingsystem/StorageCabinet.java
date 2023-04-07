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
    public StorageCabinet() { super(FURNITURE); }
    public StorageCabinet(String materialType) { super(FURNITURE, materialType); }
    public StorageCabinet(String materialType, String color) { super(FURNITURE, materialType, color); }
    public StorageCabinet(String materialType, String color, double cost) { super(FURNITURE, materialType, color, cost); }
}