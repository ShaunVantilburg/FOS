/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.furnitureorderingsystem;
import java.util.Scanner;
/**
 *
 * @author unloc
 */
public class Dimensions {
    Scanner input = new Scanner(System.in);
    double length = 0;
    double width = 0;
    double height = 0;
    
    public Dimensions() {
//        setLength();
//        setWidth();
//        setHeight();
    }
    public Dimensions(double l, double w, double h) {
//        setLength(l);
//        setWidth(w);
//        setHeight(h);
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
    private void setLength() {
        System.out.print("Furniture length in inches: ");
        double tmp = input.nextDouble();
        if (tmp > 0) length = tmp;
        else System.out.println("Invalid length.  Failed to change.");
    }
    private void setLength(double l) {
        if (l > 0) length = l;
        else System.out.println("Invalid length. Failed to change.");
    }
    private void setWidth() {
        System.out.print("Furniture width in inches: ");
        double tmp = input.nextDouble();
        if (tmp > 0) width = tmp;
        else System.out.println("Invalid width.  Failed to change.");
    }
    private void setWidth(double w) {
        if (w > 0) length = w;
        else System.out.println("Invalid width. Failed to change.");
    }
    private void setHeight() {
        System.out.print("Furniture height in inches: ");
        double tmp = input.nextDouble();
        if (tmp > 0) height = tmp;
        else System.out.println("Invalid height.  Failed to change.");
    }
    private void setHeight(double h) {
        if (h > 0) length = h;
        else System.out.println("Invalid height. Failed to change.");
    }
}
