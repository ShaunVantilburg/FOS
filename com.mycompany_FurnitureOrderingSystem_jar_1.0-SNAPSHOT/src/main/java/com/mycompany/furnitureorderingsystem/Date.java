/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.furnitureorderingsystem;

/**
 *
 * @author unloc
 */
public class Date {

    private int year = 0, month = 0, day = 0;
    
    public Date(int m, int d, int y) {
        setYear(y);
        setMonth(m);
        setDay(d);
        
        if (year == 0 || month == 0 || day == 0) {
            System.out.println("Invalid date type.");
        }
        if (month == 2 && day > 28) {
            System.out.println("Invalid day range");
        }
        if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30) {
            System.out.println("Invalid day range");
        }
    }
    
    public int getYear() {
    return year;
    }
    public int getMonth() {
    return month;
    }
    public int getDay() {
    return day;
    }
    
    public void setDate(int m, int d, int y) {
        setYear(y);
        setMonth(m);
        setDay(d);
        
        if (year == 0 || month == 0 || day == 0) {
            System.out.println("Invalid date type.");
        }
        if (month == 2 && day > 28) {
            System.out.println("Invalid day range");
        }
        if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30) {
            System.out.println("Invalid day range");
        }
    }
    private void setYear(int y) {
        if (y > 1900 && y < 2100) {
            year = y;
        }
        else System.out.println("Invalid year");
    }
    
    private void setMonth(int m) {
        if (m >= 1 && m <= 12) {
            month = m;
        }
        else System.out.println("Invalid month");
    }
    
    private void setDay(int d) {
        if (d >= 1 && d <= 31) {
            day = d;
        }
        else System.out.println("Invalid day");
    }
    
    @Override
    public String toString() {
        return day + ", " + month + ", " + year;
    }
}
