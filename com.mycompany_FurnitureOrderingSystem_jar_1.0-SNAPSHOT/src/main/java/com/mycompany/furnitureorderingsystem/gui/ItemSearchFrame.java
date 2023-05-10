package com.mycompany.furnitureorderingsystem.gui;

import com.mycompany.furnitureorderingsystem.Bed;
import com.mycompany.furnitureorderingsystem.Chair;
import com.mycompany.furnitureorderingsystem.Furniture;
import com.mycompany.furnitureorderingsystem.Sofa;
import com.mycompany.furnitureorderingsystem.database.RefreshableDatabaseAccess;
import com.mycompany.furnitureorderingsystem.database.SQLConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class ItemSearchFrame extends JFrame implements RefreshableDatabaseAccess {

    public final JLabel nameLabel;
    public final JTextField nameTxt;

    public final JButton enterBtn;
    public final JButton backBtn;
    public final JFrame parent;
    public final JPanel panel;
    public Furniture[] found;
    public final JList<Furniture> itemList;

    public ItemSearchFrame(JFrame parent){
        super("Item Search System");
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

        found = items;
        itemList = new JList<>(found);
        itemList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        itemList.setLayoutOrientation(JList.VERTICAL);
        itemList.setVisibleRowCount(-1);

        JScrollPane listScroller = new JScrollPane(itemList);
        add(listScroller);
    }
    protected static Furniture[] items = new Furniture[]{new Bed("wood","red",5,3,4,8),new Chair("wood","red",5,3,4,8),new Sofa("wood","red",5,3,4,8)};
    private Furniture[] findItem(String search){
        try {
            return SQLConnection.instance.readItems(search).toArray(new Furniture[0]);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
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
                found = findItem(nameTxt.getText());
                itemList.setListData(found);
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
