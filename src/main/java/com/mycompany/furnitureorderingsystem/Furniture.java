package com.mycompany.furnitureorderingsystem;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Shaun
 */
public abstract class Furniture {

    public Furniture(String furnitureType, String materialType, String color, double cost) {
        this.furnitureType = furnitureType;
        setMaterialType(materialType);
        setColor(color);
        setCost(cost);
    }
    
    //Member variables
    public Dimensions d = new Dimensions();
    private String materialType;
    private String color;
    private final String furnitureType;
    private double cost;
    
    //Member methods
    public double getCost() {
        return this.cost;
    }
    
    //Private initialization, safely usable in constructors
    protected final void setCost(double cost) {
        if (cost >= 0) this.cost = cost;
        else System.out.println("Invalid cost.  Change not saved."); 
    }
    
    private void setMaterialType(String mat) {
        System.out.println("What's the material type of your " + furnitureType + "?");
        System.out.print("Material type: ");
        materialType = mat;
    }
    private void setColor(String col) {
        color = col;
    }
    
    @Override
    public String toString() {
    return "This "+color+" "+furnitureType+" is made of "+materialType+" and costs $"+cost+".";
    }
}
