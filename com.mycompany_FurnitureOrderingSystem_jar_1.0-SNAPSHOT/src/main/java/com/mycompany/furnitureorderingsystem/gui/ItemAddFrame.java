package com.mycompany.furnitureorderingsystem.gui;

import com.mycompany.furnitureorderingsystem.*;
import com.mycompany.furnitureorderingsystem.database.RefreshableDatabaseAccess;
import com.mycompany.furnitureorderingsystem.database.SQLConnection;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemAddFrame extends JFrame implements RefreshableDatabaseAccess {

    public final JPanel itemPanel;
    public final JLabel matLabel;
    public final JTextField matTxt;
    public final JLabel colorLabel;
    public final JTextField colorTxt;
    public final JLabel costLabel;
    public final JTextField costTxt;
    public final JComboBox<String> itemTypeSelection;
    public final JLabel chairsLabel;
    public Furniture[] chairs = new Furniture[0];
    public final JList<Furniture> chairList;
    public final JScrollPane chairScroller;
    public final JLabel lengthLabel;
    public final JTextField lengthTxt;
    public final JLabel widthLabel;
    public final JTextField widthTxt;
    public final JLabel heightLabel;
    public final JTextField heightTxt;
    public final JLabel drawersLabel;
    public final JTextField drawersTxt;
    public final JButton enterBtn;
    public final JButton backBtn;
    public final JFrame parent;
    public ItemAddFrame(JFrame parent){
        super("Item Adder System");
        this.parent = parent;
        setLayout(new FlowLayout());
        JLabel itemTypeLabel = new JLabel("Type of Furniture:");
        itemTypeSelection = new JComboBox<>(new String[]{"Bed","Chair","Table","Sofa","Cabinet"});

        itemTypeSelection.setMaximumSize(itemTypeSelection.getPreferredSize());
        itemTypeSelection.setAlignmentX(Component.CENTER_ALIGNMENT);
        itemTypeSelection.addActionListener(new ItemTypeHandler());

        setLayout(new GridLayout(1,1));

        itemPanel = new JPanel();
        itemPanel.setLayout(new GridLayout(7,2));

        matLabel = new JLabel("Material Type: ");
        matTxt = new JTextField(10);

        colorLabel = new JLabel("Color: ");
        colorTxt = new JTextField(10);

        costLabel = new JLabel("Cost: ");
        costTxt = new JTextField(10);

        lengthLabel = new JLabel("Length: ");
        lengthTxt = new JTextField(10);

        widthLabel = new JLabel("Width: ");
        widthTxt = new JTextField(10);

        heightLabel = new JLabel("Height: ");
        heightTxt = new JTextField(10);

        drawersLabel = new JLabel("Number of Drawers:");
        drawersTxt = new JTextField(10);

        chairsLabel = new JLabel("Choose Chairs for Dining Table Set:");

        itemPanel.add(itemTypeLabel);
        itemPanel.add(itemTypeSelection);
        itemPanel.add(matLabel);
        itemPanel.add(matTxt);
        itemPanel.add(colorLabel);
        itemPanel.add(colorTxt);
        itemPanel.add(costLabel);
        itemPanel.add(costTxt);
        itemPanel.add(lengthLabel);
        itemPanel.add(lengthTxt);
        itemPanel.add(widthLabel);
        itemPanel.add(widthTxt);

        reload();

        chairList = new JList<>(chairs);
        chairList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        chairList.setLayoutOrientation(JList.VERTICAL);
        chairList.setVisibleRowCount(-1);
        chairList.addListSelectionListener(new ItemSelectionListener());

        chairScroller = new JScrollPane(chairList);

        add(itemPanel);

        enterBtn = new JButton("Enter");
        itemPanel.add(enterBtn);
        enterBtn.addMouseListener(new MouseHandler("Enter"));
        backBtn = new JButton("Back");
        itemPanel.add(backBtn);
        backBtn.addMouseListener(new MouseHandler("Back"));
    }

    @Override
    public void reload() {
        List<Furniture> chairsList;
        try {
            chairsList = SQLConnection.instance.readItems("A.Type","chair");
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        chairs = chairsList.toArray(new Furniture[0]);
        if (this.chairList!=null)
            chairList.setListData(chairs);
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
                String mat = matTxt.getText();
                String color = colorTxt.getText();
                double cost = Double.parseDouble(costTxt.getText());
                double length = Double.parseDouble(lengthTxt.getText());
                double width = Double.parseDouble(widthTxt.getText());
                double height = Double.parseDouble(heightTxt.getText());
                int drawers = Integer.parseInt(drawersTxt.getText());
                Furniture item;
                switch ((String) Objects.requireNonNull(itemTypeSelection.getSelectedItem())){
                    case "Bed" -> item = new Bed(mat,color,cost,length,width,height);
                    case "Chair" -> item = new Chair(mat,color,cost,length,width,height);
                    case "Table" -> {item = new DiningTable(mat,color,cost,length,width,height);
                        ((DiningTable) item).listOfChairs.addAll(List.of((Chair[]) chairs));}
                    case "Sofa" -> item = new Sofa(mat,color,cost,length,width,height);
                    case "Cabinet" -> item = new StorageCabinet(mat,color,cost,length,width,height, drawers);
                    default -> item = null;
                }
                if (item!=null)
                    try {
                        SQLConnection.instance.writeItem(item);
                        reload();
                    } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                    }
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

    private class ItemTypeHandler implements ActionListener {
        String previousType;
        @Override
        public void actionPerformed(ActionEvent e) {
            String type = (String) itemTypeSelection.getSelectedItem();
            if (!Objects.equals(type, previousType)) {
                if (Objects.equals(previousType, "Table")) {
                    setLayout(new GridLayout(1,1));
                    itemPanel.setLayout(new GridLayout(7,2));
                    remove(chairScroller);
                    itemPanel.remove(chairsLabel);
                } else if (Objects.equals(previousType, "Cabinet")){
                    itemPanel.setLayout(new GridLayout(7,1));
                    itemPanel.remove(drawersLabel);
                    itemPanel.remove(drawersTxt);
                }
                previousType = type;
                if (Objects.equals(type, "Table")) {
                    setLayout(new GridLayout(2,1));
                    add(chairScroller);
                    itemPanel.setLayout(new GridLayout(8,2));
                    itemPanel.add(chairsLabel);
                } else if (Objects.equals(type, "Cabinet")) {
                    itemPanel.setLayout(new GridLayout(2,1));
                    itemPanel.add(drawersLabel);
                    itemPanel.add(drawersTxt);
                }
            }
        }
    }

    private int[] storedIndices = new int[0];
    private boolean adjusting = false;

    private class ItemSelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (adjusting)
                return;
            if (storedIndices==chairList.getSelectedIndices())
                return;
//            int firstIndex = e.getFirstIndex();
//            int lastIndex = e.getLastIndex();
            boolean isAdjusting = e.getValueIsAdjusting();
            if (isAdjusting)
                return;
            adjusting = true;
            int[] oldSelection = storedIndices;
            int[] newSelection = chairList.getSelectedIndices();
            ArrayList<Integer> newIndices = new ArrayList<>();
            for (int i = 0; i < chairs.length; i++){
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
            chairList.setSelectedIndices(storedIndices);
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
