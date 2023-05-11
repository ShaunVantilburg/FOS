package com.mycompany.furnitureorderingsystem;

import java.util.Date;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Shaun
 */
public class Order {
    Customer customer;
    public Date dateOfOrder;
    Furniture[] list_of_items;
    double total_price = 0;
   
    public Order(Customer customer, Date date, Furniture[] list) {
        this.customer = customer;
        this.dateOfOrder = date;
        this.list_of_items = list;
        for (Furniture f : list_of_items) {
            total_price += f.getCost();
        }
    }
   public Order() {
       this.customer = new Customer(0, "John", dateOfOrder, "blah");
       this.dateOfOrder = new Date(1992,9,1);
       this.list_of_items = new Furniture[1];
       list_of_items[0] = new Chair("wood", "red", 20, 20, 20, 60);
   }
    @Override
    public String toString() {
        String list = "";
        for (var f : list_of_items) {
            list = list.concat(f.toString());
        }
        return "Customer name: " + customer.name() +
                "    Date of Order: " + dateOfOrder +
                "    List of items: " + list +
                "    Total price: " + total_price;        
    };
}