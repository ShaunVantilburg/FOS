package com.mycompany.furnitureorderingsystem;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.util.Scanner;
/**
 *
 * @author Shaun
 */
public abstract class Furniture {
//    Scanner input = new Scanner(System.in);
    //Constructors
    public Furniture(String furnitureType) {
        this.furnitureType = furnitureType;
        setMaterialType();
        setColor();
        setCost();
    }
    public Furniture(String furnitureType, String materialType) {
        this.furnitureType = furnitureType;
        setMaterialType(materialType);
        setColor();
        setCost();
    }
    public Furniture(String furnitureType, String materialType, String color) {
        this.furnitureType = furnitureType;
        setMaterialType(materialType);
        setColor(color);
        setCost();
    }
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
//        if (cost >= 0) this.cost = cost;
//
//        else System.out.println("Invalid cost.  Change not saved.");
    }
    
    private void setMaterialType() {
//        System.out.println("What's the material type of your " + furnitureType + "?");
//        System.out.print("Material type: ");
//        materialType = input.nextLine();
    }
    protected final void setMaterialType(String m) {
        materialType = m;
    }
    private void setColor() {
//        System.out.println("What color is your " + furnitureType + "?");
//        System.out.print("Color: ");
//        color = input.nextLine();
    }
    protected final void setColor(String c) {
        color = c;
    }
    private void setCost() {
//        System.out.println("How much does your " + furnitureType + " cost?");
//        System.out.print("Cost: $");
//        setCost(input.nextDouble());
    }
    @Override
    public String toString() {
    return "This "+color+" "+furnitureType+" is made of "+materialType+" and costs $"+cost+".";
    }
}
