package com.mycompany.furnitureorderingsystem.gui;

import com.mycompany.furnitureorderingsystem.*;
import com.mycompany.furnitureorderingsystem.database.RefreshableDatabaseAccess;
import com.mycompany.furnitureorderingsystem.database.SQLConnection;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;


public class CreateOrderFrame extends JFrame implements RefreshableDatabaseAccess {

    public final JButton enterBtn;
    public final JButton backBtn;
    public final JFrame parent;
    protected static String[] customers = SQLConnection.customers();
    protected String[] customerArray = customers;
    protected final JComboBox<String> customerCB;
    public final Furniture[] items;
    public final JLabel dateLabel;
    public final JTextField dateTxt;
    public final JList<Furniture> itemList;

    public CreateOrderFrame(JFrame parent){
        super("Create Order System");
        this.parent = parent;
        setLayout(new GridLayout(5,1));

        JPanel customerPanel = new JPanel();
        customerPanel.setLayout(new BoxLayout(customerPanel, BoxLayout.Y_AXIS));

        add(customerPanel);

        JLabel customerLbl = new JLabel("Select a customer");
        customerLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        customerPanel.add(customerLbl);



        customerCB = new JComboBox<>(customerArray);

        customerCB.setMaximumSize(customerCB.getPreferredSize());
        customerCB.setAlignmentX(Component.CENTER_ALIGNMENT);
        customerPanel.add(customerCB);

        JPanel p = new JPanel();
        p.setLayout(new GridLayout(1,2));
        dateLabel = new JLabel("Enter Date: ");
        p.add(dateLabel);
        dateTxt = new JTextField();
        p.add(dateTxt);
        add(p);

        JLabel itemLbl = new JLabel("Select items:");
        itemLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(itemLbl);

        items = SQLConnection.findItems();
        itemList = new JList<>(items);
        itemList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        itemList.setLayoutOrientation(JList.VERTICAL);
        itemList.setVisibleRowCount(-1);
        itemList.addListSelectionListener(new ItemSelectionListener());

        JScrollPane itemScroller = new JScrollPane(itemList);
        add(itemScroller);

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new GridLayout(1,2));

        enterBtn = new JButton("Enter");
        btnPanel.add(enterBtn);
        enterBtn.addMouseListener(new MouseHandler("Enter"));
        backBtn = new JButton("Back");
        btnPanel.add(backBtn);
        backBtn.addMouseListener(new MouseHandler("Back"));
        add(btnPanel);
    }

    @Override
    public void reload() {

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

                Furniture[] furnitureList = new Furniture[itemList.getSelectedValuesList().size()];
                furnitureList = itemList.getSelectedValuesList().toArray(furnitureList);
                int[] furnitureIndices = itemList.getSelectedIndices();
                int customerIndex = customerCB.getSelectedIndex();

                try {
                    DateFormat format = new SimpleDateFormat("MM/DD/YYYY");
                    Date orderDate = format.parse(dateTxt.getText());
                    java.sql.Date sqlDate = new java.sql.Date(orderDate.getTime());
                    Order new_order = new Order(SQLConnection.customerAt(customerIndex),sqlDate,furnitureList);
                    SQLConnection.addOrder(sqlDate, furnitureIndices, customerIndex);
                    } catch(Exception ex) {System.out.println(ex);}
                
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
    private int[] storedIndices = new int[0];
    private boolean adjusting = false;
    private class ItemSelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (adjusting)
                return;
            if (storedIndices==itemList.getSelectedIndices())
                return;
            int firstIndex = e.getFirstIndex();
            int lastIndex = e.getLastIndex();
            boolean isAdjusting = e.getValueIsAdjusting();
            if (isAdjusting)
                return;
            adjusting = true;
            int[] oldSelection = storedIndices;
            int[] newSelection = itemList.getSelectedIndices();
            ArrayList<Integer> newIndices = new ArrayList<>();
            for (int i = 0; i < items.length; i++){
                boolean inOld = containsIndex(oldSelection,i);
                boolean inNew = containsIndex(newSelection,i);
                if (!inOld & inNew){
                    newIndices.add(i);
                } else if (inOld & !inNew) {
                    newIndices.add(i);
                }
            }
            newSelection = new int[newIndices.size()];
            for (int i=0;i< newIndices.size();i++)
                newSelection[i] = newIndices.get(i);
            storedIndices = newSelection;
            itemList.setSelectedIndices(storedIndices);
            adjusting = false;
        }

        private boolean containsIndex(int[] A, int index){
            for (int a:A){
                if (a==index)
                    return true;
            }
            return false;
        }
    }
}
