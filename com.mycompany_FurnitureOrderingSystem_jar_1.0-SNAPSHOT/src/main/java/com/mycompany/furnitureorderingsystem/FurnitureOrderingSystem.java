/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.furnitureorderingsystem;

import com.mycompany.furnitureorderingsystem.gui.GUIMain;

import java.util.ArrayList;

/**
 *
 * @author Shaun
 */
public class FurnitureOrderingSystem {
    // Fake databases storage for testing
    public static final ArrayList<Customer> customerDB = new ArrayList<>();
    public static final ArrayList<Furniture> itemDB = new ArrayList<>();
    public static final ArrayList<Order> orderDB = new ArrayList<>();

    public static void main(String[] args) {
        GUIMain.open();
    }
}