/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.furnitureorderingsystem;

import com.mycompany.furnitureorderingsystem.database.SQLConnection;
import com.mycompany.furnitureorderingsystem.gui.GUIMain;

import java.sql.SQLException;

/**
 *
 * @author Shaun
 */
public class FurnitureOrderingSystem {
    public static void main(String[] args) {
        try {
            new SQLConnection("jdbc:mysql://localhost/fos_database", "CS3700", "CS3700");
        } catch (SQLException sqlException){
            sqlException.printStackTrace();
            throw new RuntimeException("Connection failed: "+sqlException.getMessage());
        }
        GUIMain.open();
    }
}