package com.mycompany.furnitureorderingsystem.gui;

import com.mycompany.furnitureorderingsystem.Customer;
import com.mycompany.furnitureorderingsystem.FurnitureOrderingSystem;
import com.mycompany.furnitureorderingsystem.database.RefreshableDatabaseAccess;
import com.mycompany.furnitureorderingsystem.database.SQLConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class CustomerAddFrame extends JFrame implements RefreshableDatabaseAccess {

    public final JLabel nameLabel;
    public final JTextField nameTxt;
    public final JLabel dobLabel;
    public final JTextField dobTxt;
    public final JLabel addressLabel;
    public final JTextField addressTxt;

    public final JButton enterBtn;
    public final JButton backBtn;
    public final JFrame parent;

    public CustomerAddFrame(JFrame parent){
        super("Customer Adder System");
        this.parent = parent;
        setLayout(new GridLayout(4,2));

        nameLabel = new JLabel("Name: ");
        Font font = new Font("Serif", Font.BOLD, 14);
        nameLabel.setFont(font);
        add(nameLabel);

        nameTxt = new JTextField(10);
        nameTxt.setFont(font);
        add(nameTxt);

        dobLabel = new JLabel("Date of Birth: ");
        dobLabel.setFont(font);
        add(dobLabel);

        dobTxt = new JTextField(10);
        dobTxt.setFont(font);
        add(dobTxt);

        addressLabel = new JLabel("Address: ");
        addressLabel.setFont(font);
        add(addressLabel);

        addressTxt = new JTextField(10);
        addressTxt.setFont(font);
        add(addressTxt);

        enterBtn = new JButton("Enter");
        add(enterBtn);
        enterBtn.addMouseListener(new MouseHandler("Enter"));
        backBtn = new JButton("Back");
        add(backBtn);
        backBtn.addMouseListener(new MouseHandler("Back"));

    }

    @Override
    public void reload() {
        // Unused
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
                DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                Customer customer;
                try {
                    customer = new Customer(1,nameTxt.getText(), format.parse(dobTxt.getText()),addressTxt.getText());
                    SQLConnection.instance.writeCustomer(customer);
                } catch (ParseException | SQLException ex) {
                    ex.printStackTrace();
                    throw new RuntimeException(ex);
                }
                nameTxt.setText(""); dobTxt.setText(""); addressTxt.setText("");
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
