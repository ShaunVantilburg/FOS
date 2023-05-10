package com.mycompany.furnitureorderingsystem.gui;

import com.mycompany.furnitureorderingsystem.Customer;
import com.mycompany.furnitureorderingsystem.database.RefreshableDatabaseAccess;
import com.mycompany.furnitureorderingsystem.database.SQLConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class CustomerSearchFrame extends JFrame implements RefreshableDatabaseAccess {

    public final JLabel nameLabel;
    public final JTextField nameTxt;

    public final JButton enterBtn;
    public final JButton backBtn;
    public final JFrame parent;
    public final JPanel panel;
    public Customer[] found;
    public final JList<Customer> customerList;

    public CustomerSearchFrame(JFrame parent){
        super("Customer Search System");
        this.parent = parent;
        setLayout(new GridLayout(2,1));
        panel = new JPanel();

        nameLabel = new JLabel("Name: ");
        Font nameFont = new Font("Serif", Font.BOLD, 14);
        nameLabel.setFont(nameFont);
        panel.add(nameLabel);

        nameTxt = new JTextField(10);
        nameTxt.setFont(nameFont);
        panel.add(nameTxt);

        enterBtn = new JButton("Search");
        panel.add(enterBtn);
        enterBtn.addMouseListener(new MouseHandler("Enter"));
        backBtn = new JButton("Back");
        panel.add(backBtn);
        backBtn.addMouseListener(new MouseHandler("Back"));
        panel.setLayout(new GridLayout(2,2));
        add(panel);

        reload();
        customerList = new JList<>(found);
        customerList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        customerList.setLayoutOrientation(JList.VERTICAL);
        customerList.setVisibleRowCount(-1);

        JScrollPane listScroller = new JScrollPane(customerList);
        add(listScroller);


    }
    private Customer[] findCustomer(String search){
        try {
            var found = SQLConnection.instance.readCustomers(search);
            return found.toArray(new Customer[0]);
        } catch (SQLException ex){
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void reload() {
        found = findCustomer("");
        if (this.customerList!=null)
            customerList.setListData(found);
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
                found = findCustomer(nameTxt.getText());
                customerList.setListData(found);
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
}
