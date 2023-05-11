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

    public Furniture(String furnitureType, String materialType, String color, double cost, double l, double w, double h) {
        dimensions = new Dimensions(l, w, h);
        this.furnitureType = furnitureType;
        setMaterialType(materialType);
        setColor(color);
        setCost(cost);
    }

    public static Furniture getFurniture(String furnitureType, String materialType, String color, double cost, double l, double w, double h, int drawers){
        Furniture item = null;
        switch (furnitureType) {
            case "bed"-> item = new Bed(materialType, color, cost, l, w, h);
            case "chair"-> item = new Chair(materialType,color,cost,l,w,h);
            case "dining table"-> item = new DiningTable(materialType,color,cost,l,w,h);
            case "sofa"-> item = new Sofa(materialType,color,cost,l,w,h);
            case "storage cabinet"-> item = new StorageCabinet(materialType,color,cost,l,w,h,drawers);
        }

        return item;
    }
    
    //Member variables
    public Dimensions dimensions;
    private String materialType;
    private String color;
    private final String furnitureType;
    private double cost;
    
    //Member methods
    public double getCost() {
        return this.cost;
    }

    public String getFurnitureType(){
        return furnitureType;
    }

    public String getMaterialType(){
        return materialType;
    }

    public String getColor(){
        return color;
    }
    
    //Private initialization, safely usable in constructors
    protected final void setCost(double cost) {
        if (cost >= 0) this.cost = cost;
        else System.out.println("Invalid cost.  Change not saved."); 
    }
    
    private void setMaterialType(String mat) {
        materialType = mat;
    }
    private void setColor(String col) {
        color = col;
    }
    
    @Override
    public String toString() {
        String typess = "Furniture Type: " + furnitureType + ", ";
        String matss = "Material Type: " + materialType + ", ";
        String colorss = "Color: " + color + ", ";
        String costss = "Cost: $" + cost + ", ";
        String lengthss = "Length: " + dimensions.length + " in, ";
        String widthss = "Width: " + dimensions.width + " in, ";
        String heightss = "Height: " + dimensions.height + " in";
        return typess + matss + colorss + costss + lengthss + widthss + heightss;
    }
}
