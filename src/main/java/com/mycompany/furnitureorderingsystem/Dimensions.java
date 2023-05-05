/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.furnitureorderingsystem;
/**
 *
 * @author unloc
 */
public class Dimensions {
    double length = 0;
    double width = 0;
    double height = 0;
    
    public Dimensions(double l, double w, double h) {
        setDimensions(l, w, h);
    }
    public double getLength() {
        return length;
    }
    public double getWidth() {
        return width;
    }
    public double getHeight() {
        return height;
    }
    private void setDimensions(double l, double w, double h) {
        setLength(l);
        setWidth(w);
        setHeight(h);
    }
    private void setLength(double l) {
        if (l > 0) length = l;
        else System.out.println("Invalid length. Failed to change.");
    }
    private void setWidth(double w) {
        if (w > 0) width = w;
        else System.out.println("Invalid width. Failed to change.");
    }
    private void setHeight(double h) {
        if (h > 0) height = h;
        else System.out.println("Invalid height. Failed to change.");
    }
}
