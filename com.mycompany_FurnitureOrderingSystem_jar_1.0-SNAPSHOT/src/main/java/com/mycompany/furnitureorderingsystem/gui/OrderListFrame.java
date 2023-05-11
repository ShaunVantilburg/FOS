package com.mycompany.furnitureorderingsystem.gui;

import com.mycompany.furnitureorderingsystem.Order;
import com.mycompany.furnitureorderingsystem.database.RefreshableDatabaseAccess;
import com.mycompany.furnitureorderingsystem.database.SQLConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class OrderListFrame extends JFrame implements RefreshableDatabaseAccess {

    public final JFrame parent;
    public final JButton backBtn;
    public final JList<Order> orderList;

    public OrderListFrame(JFrame parent){
        super("Order List System");
        this.parent = parent;
        setLayout(new GridLayout(2,1));

        reload();
        orderList = new JList<>(orders);
        orderList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        orderList.setLayoutOrientation(JList.VERTICAL);
        orderList.setVisibleRowCount(-1);

        JScrollPane listScroller = new JScrollPane(orderList);
        add(listScroller);

        backBtn = new JButton("Back");
        add(backBtn);
        backBtn.addMouseListener(new MouseHandler());
    }
    protected static Order[] orders = new Order[0];

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
        @Override
        public void mouseClicked(MouseEvent e) {
            GUIMain.setActiveFrame(parent);
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
