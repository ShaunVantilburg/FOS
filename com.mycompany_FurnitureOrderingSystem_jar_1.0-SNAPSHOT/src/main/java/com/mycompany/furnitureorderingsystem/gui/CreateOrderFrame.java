package com.mycompany.furnitureorderingsystem.gui;

import com.mycompany.furnitureorderingsystem.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreateOrderFrame extends JFrame {

    public final JButton enterBtn;
    public final JButton backBtn;
    public final JFrame parent;
    protected static Customer[] customers = {new Customer("Shaun"), new Customer("Josh"), new Customer("Mario")};
    protected Customer[] customerArray = customers;
    protected final JComboBox<Customer> customerCB;
    public final JPanel itemsPanel;
    protected ArrayList<Furniture> items = new ArrayList<>();
    public CreateOrderFrame(JFrame parent){
        super("Create Order System");
        this.parent = parent;
        setLayout(new GridLayout(7,1));

        JPanel customerPanel = new JPanel();
        customerPanel.setLayout(new BoxLayout(customerPanel, BoxLayout.Y_AXIS));

        add(customerPanel);

        JLabel customerLbl = new JLabel("Select a customer");
        customerLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        customerPanel.add(customerLbl);

        // TODO: get orders from database

        customerCB = new JComboBox<>(customerArray);

        customerCB.setMaximumSize(customerCB.getPreferredSize());
        customerCB.setAlignmentX(Component.CENTER_ALIGNMENT);
        customerPanel.add(customerCB);

        // TODO: add date picker

        itemsPanel = new JPanel();

        JPanel itemSelectPanel = new JPanel();
        itemSelectPanel.setLayout(new BoxLayout(itemSelectPanel, BoxLayout.Y_AXIS));

        itemsPanel.add(itemSelectPanel);
        add(itemsPanel);

        JLabel itemLbl = new JLabel("Select an item");
        itemLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        customerPanel.add(itemLbl);

        // TODO: get orders from database

        Furniture[] items = new Furniture[]{new Bed("red"),new Chair(),new Sofa()};

        final JComboBox<Furniture> itemCB = new JComboBox<>(items);

        itemCB.setMaximumSize(itemCB.getPreferredSize());
        itemCB.setAlignmentX(Component.CENTER_ALIGNMENT);
        itemCB.addActionListener(new ItemListHandler(itemCB));
        itemSelectPanel.add(itemCB);

        enterBtn = new JButton("Enter");
        add(enterBtn);
        enterBtn.addMouseListener(new MouseHandler("Enter"));
        backBtn = new JButton("Back");
        add(backBtn);
        backBtn.addMouseListener(new MouseHandler("Back"));

    }

    private class MouseHandler implements MouseListener {
        private final String selector;
        MouseHandler(String selector){
            this.selector = selector;
        }
        @Override
        public void mouseClicked(MouseEvent e) {
            if (Objects.equals(this.selector, "Back")){
                GUIMain.setActiveFrame(parent);
            } else {
                // TODO: Save Order to Database
            }
        }
        @Override
        public void mousePressed(MouseEvent e) {
        }
        @Override
        public void mouseReleased(MouseEvent e) {
        }
        @Override
        public void mouseEntered(MouseEvent e) {}
        @Override
        public void mouseExited(MouseEvent e) {}
    }

    private class ItemListHandler implements ActionListener {
        private final JComboBox<Furniture> itemCB;
        ItemListHandler(JComboBox<Furniture> itemCB){
            this.itemCB = itemCB;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            var item = itemCB.getSelectedItem();
            itemCB.removeItem(item);
            items.add((Furniture) item);
            JLabel itemLabel = new JLabel(item.toString());
            itemsPanel.add(itemLabel);
            itemsPanel.setLayout(new GridLayout(itemsPanel.getComponentCount(),1));
            itemsPanel.doLayout();
        }
    }
}
