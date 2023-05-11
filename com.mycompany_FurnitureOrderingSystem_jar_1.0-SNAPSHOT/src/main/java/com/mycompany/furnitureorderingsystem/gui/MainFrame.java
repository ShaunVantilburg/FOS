package com.mycompany.furnitureorderingsystem.gui;

import com.mycompany.furnitureorderingsystem.database.RefreshableDatabaseAccess;
import com.mycompany.furnitureorderingsystem.gui.CustomerAddFrame;
import com.mycompany.furnitureorderingsystem.gui.GUIMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MainFrame extends JFrame implements RefreshableDatabaseAccess {

    public final JButton[] buttons = new JButton[7];

    public MainFrame(){
        super("Furniture Ordering System");
        setLayout(new GridLayout(7,1));

        addButton(0,"Add a new customer.",new CustomerAddFrame(this));
        addButton(1, "Add a new item.", new ItemAddFrame(this));
        addButton(2,"Create an order.", new CreateOrderFrame(this));
        addButton(3,"Search for a customer and display information.", new CustomerSearchFrame(this));
        addButton(4,"Search for an item and display information.", new ItemSearchFrame(this));
        addButton(5, "Search for an order and display information.", new OrderSearchFrame(this));
        addButton(6, "List all orders.", new OrderListFrame(this));
    }

    private void addButton(int index, String text, JFrame frame){
        buttons[index] = new JButton(text);
        add(buttons[index]);
        buttons[index].addMouseListener(new MouseHandler(frame));
    }

    @Override
    public void reload() {

    }

    private record MouseHandler(JFrame frame) implements MouseListener {
        @Override
            public void mouseClicked(MouseEvent e) {
                GUIMain.setActiveFrame(frame);
            }

        @Override
            public void mousePressed(MouseEvent e) {
            }

        @Override
            public void mouseReleased(MouseEvent e) {
            }

        @Override
            public void mouseEntered(MouseEvent e) {
        }

        @Override
            public void mouseExited(MouseEvent e) {
        }
        }
}
