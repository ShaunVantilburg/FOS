package com.mycompany.furnitureorderingsystem.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;

public class CustomerAddFrame extends JFrame {

    public final JLabel nameLabel;
    public final JTextField nameTxt;

    public final JButton enterBtn;
    public final JButton backBtn;
    public final JFrame parent;

    public CustomerAddFrame(JFrame parent){
        super("Customer Adder System");
        this.parent = parent;
        setLayout(new FlowLayout());

        nameLabel = new JLabel("Name: ");
        Font nameFont = new Font("Serif", Font.BOLD, 14);
        nameLabel.setFont(nameFont);
        add(nameLabel);

        nameTxt = new JTextField(10);
        nameTxt.setFont(nameFont);
        add(nameTxt);

        enterBtn = new JButton("Enter");
        add(enterBtn);
        enterBtn.addMouseListener(new MouseHandler("Enter"));
        backBtn = new JButton("Back");
        add(backBtn);
        backBtn.addMouseListener(new MouseHandler("Back"));

    }

    private class MouseHandler implements MouseListener {
        private String selector;
        MouseHandler(String selector){
            this.selector = selector;
        }
        @Override
        public void mouseClicked(MouseEvent e) {
            if (Objects.equals(this.selector, "Back")){
                GUIMain.setActiveFrame(parent);
            } else {
                // TODO: Save Customer to Database
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
