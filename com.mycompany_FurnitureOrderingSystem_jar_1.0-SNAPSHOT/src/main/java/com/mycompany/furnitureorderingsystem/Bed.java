package com.mycompany.furnitureorderingsystem;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Shaun
 */
public class Bed extends Furniture {
private static final String FURNITURE = "bed";
    public Bed() { super(FURNITURE); }
    public Bed(String materialType) { super(FURNITURE, materialType); }
    public Bed(String materialType, String color) { super(FURNITURE, materialType, color); }
    public Bed(String materialType, String color, double cost) { super(FURNITURE, materialType, color, cost); }
}