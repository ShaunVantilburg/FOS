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
    Scanner input = new Scanner(System.in);
    //Constructors
    public Furniture(String furnitureType) {
        this.furnitureType = furnitureType;
        materialQuery();
        colorQuery();
        costQuery();
    }
    public Furniture(String furnitureType, String materialType) {
        this.furnitureType = furnitureType;
        this.materialType = materialType;
        colorQuery();
        costQuery();
    }
    public Furniture(String furnitureType, String materialType, String color) {
        this.furnitureType = furnitureType;
        this.materialType = materialType;
        this.color = color;
        costQuery();
    }
    public Furniture(String furnitureType, String materialType, String color, double cost) {
        this.furnitureType = furnitureType;
        this.materialType = materialType;
        this.color = color;
        initCost(cost);
    }
    
    //Member variables
    public String materialType;
    public String color;
    public String furnitureType;
    private double cost;
    
    //Member methods
    public double getCost() {
        return this.cost;
    }
    
    //Private initialization, safely usable in constructors
    private void initCost(double cost) {
        if (cost > 0) {
            this.cost = cost;
            System.out.println("Cost successfully set to $" + cost);
        }
        else System.out.println("Invalid cost.  Change not saved.");
    }
    
    //Protected setCost, usable from subclasses
    protected void setCost(double cost) { initCost(cost); }
    
    private void materialQuery() {
        System.out.println("What's the material type of your " + furnitureType + "?");
        System.out.print("Material type: ");
        materialType = input.nextLine();
    }
    private void colorQuery() {
        System.out.println("What color is your " + furnitureType + "?");
        System.out.print("Color: ");
        color = input.nextLine();
    }
    private void costQuery() {
        System.out.println("How much does your " + furnitureType + " cost?");
        System.out.print("Cost: $");
        setCost(input.nextDouble());
    }
    
    @Override
    public String toString() {
    return "This "+color+" "+furnitureType+" is made of "+materialType+" and costs $"+cost+".";
    }
}
