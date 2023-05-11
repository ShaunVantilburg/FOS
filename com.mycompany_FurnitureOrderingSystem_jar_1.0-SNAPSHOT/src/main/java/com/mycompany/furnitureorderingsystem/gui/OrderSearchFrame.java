package com.mycompany.furnitureorderingsystem.gui;

import com.mycompany.furnitureorderingsystem.*;
import com.mycompany.furnitureorderingsystem.database.RefreshableDatabaseAccess;
import com.mycompany.furnitureorderingsystem.database.SQLConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class OrderSearchFrame extends JFrame implements RefreshableDatabaseAccess {

    public final JLabel nameLabel;
    public final JTextField nameTxt;

    public final JButton enterBtn;
    public final JButton backBtn;
    public final JFrame parent;
    public final JPanel panel;
    public Order[] found;
    public final JList<Order> orderList;

    public OrderSearchFrame(JFrame parent){
        super("Order Search System");
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

        found = orders;
        orderList = new JList<>(found);
        orderList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        orderList.setLayoutOrientation(JList.VERTICAL);
        orderList.setVisibleRowCount(-1);

        JScrollPane listScroller = new JScrollPane(orderList);
        add(listScroller);
    }
    protected static Order[] orders = new Order[0];
    private Order[] findItem(String search){
        try {
            return SQLConnection.instance.readOrders(search).toArray(new Order[0]);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void reload() {
        try {
            orders = SQLConnection.instance.readOrders("").toArray(new Order[0]);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        if (orderList!=null)
            orderList.setListData(orders);
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
                found = findItem(nameTxt.getText());
                orderList.setListData(found);
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
