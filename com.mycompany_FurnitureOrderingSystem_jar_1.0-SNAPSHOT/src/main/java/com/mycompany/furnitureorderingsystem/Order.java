package com.mycompany.furnitureorderingsystem;

import java.util.Date;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Shaun
 */
public class Order {
    private final Customer customer;
    private final Date date_of_order;
    private final List<Furniture> list_of_items;
    double total_price = 0;

    public Order(Customer customer, Date date, List<Furniture> list) {
        this.customer = customer;
        this.date_of_order = date;
        this.list_of_items = list;
        for (Furniture f : list_of_items) {
            total_price += f.getCost();
        }
    }

    public Date getDate(){
        return date_of_order;
    }

    public Customer getCustomer(){
        return customer;
    }

    public List<Furniture> getItems(){
        return list_of_items;
    }

    public void addItem(Furniture item){
        list_of_items.add(item);
        total_price += item.getCost();
    }

    public double getTotalCost(){
        return total_price;
    }

    @Override
    public String toString() {
        String list = "[";
        for (var f : list_of_items) {
            list = list.concat(f.toString()).concat(", ");
        }
        list += "] ";
        return "Customer name: " + customer.name() +
                " Date of Order: " + date_of_order +
                " List of items: " + list +
                " Total price: " + total_price;
    }
}
