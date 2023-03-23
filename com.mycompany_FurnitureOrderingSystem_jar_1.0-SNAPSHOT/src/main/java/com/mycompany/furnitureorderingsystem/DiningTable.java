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
    public DiningTable() { super(FURNITURE); }
    public DiningTable(String materialType) { super(FURNITURE, materialType); }
    public DiningTable(String materialType, String color) { super(FURNITURE, materialType, color); }
    public DiningTable(String materialType, String color, double cost) { super(FURNITURE, materialType, color, cost); }
}