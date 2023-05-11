package com.mycompany.furnitureorderingsystem.gui;

import com.mycompany.furnitureorderingsystem.database.RefreshableDatabaseAccess;

import javax.swing.*;

public class GUIMain {
    public static JFrame activeFrame;

    public static void open(){
        activeFrame = new MainFrame();
        activeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        activeFrame.setSize(600,450);
        activeFrame.setVisible(true);
    }

    public static void setActiveFrame(JFrame frame){
        activeFrame.setVisible(false);
        var point = activeFrame.getLocation();
        var size = activeFrame.getSize();
        activeFrame = frame;
        activeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        activeFrame.setSize(size);
        activeFrame.setLocation(point);
        activeFrame.setVisible(true);
        ((RefreshableDatabaseAccess) activeFrame).reload();
    }
}
