package com.mycompany.furnitureorderingsystem;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Shaun
 */
public class Chair extends Furniture {
private static final String FURNITURE = "chair";
    public Chair() { super(FURNITURE); }
    public Chair(String materialType) { super(FURNITURE, materialType); }
    public Chair(String materialType, String color) { super(FURNITURE, materialType, color); }
    public Chair(String materialType, String color, double cost) { super(FURNITURE, materialType, color, cost); }
}