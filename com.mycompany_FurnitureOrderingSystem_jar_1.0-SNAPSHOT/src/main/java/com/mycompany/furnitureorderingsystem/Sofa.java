package com.mycompany.furnitureorderingsystem;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Shaun
 */
public class Sofa extends Furniture {
    private static final String FURNITURE = "sofa";
    public Sofa() { super(FURNITURE); }
    public Sofa(String materialType) { super(FURNITURE, materialType); }
    public Sofa(String materialType, String color) { super(FURNITURE, materialType, color); }
    public Sofa(String materialType, String color, double cost) { super(FURNITURE, materialType, color, cost); }
}