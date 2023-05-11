package com.mycompany.furnitureorderingsystem.database;

import com.mycompany.furnitureorderingsystem.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SQLConnection {
    public static SQLConnection instance;
    private static Connection conn;

    public SQLConnection(String url, String username, String password) throws SQLException {
        conn = DriverManager.getConnection(url, username, password);
        instance = this;
    }
    
        public static Customer customerAt(int customerIndex) throws SQLException {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM customers WHERE id = " + customerIndex);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                String name = rs.getString("name");
                Date dob = rs.getDate("dob");
                int id = rs.getInt("id");
                stmt = conn.prepareStatement("Select * FROM address WHERE addressID=" + rs.getInt("addressID"));
                ResultSet rs2 = stmt.executeQuery();
                while(rs2.next()) {
                    String street = rs2.getString("street");
                    String city = rs2.getString("city");
                    String state = rs2.getString("state");
                    String zip = rs2.getString("zip");   
                    return new Customer(id, name, dob, street + ", " + city + ", " + state + ", " + zip);
                }
            }
            return new Customer(1,"n",new Date(1992, 12, 1), "jon");
        }

    public static void addOrder(Date orderDate, int[] furnitureIndices, int customerIndex) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO orders (DateOfOrder,CustomerID) VALUES(?,?)");
        stmt.setDate(1,new Date(orderDate.getTime()));
        stmt.setInt(2, customerIndex+1);
        stmt.executeUpdate();
        stmt = conn.prepareStatement("SELECT MAX(OrderID) FROM orders");
        ResultSet rs = stmt.executeQuery();
        rs.next();
        int orderIndex = rs.getInt("Max(OrderID)");
        for (int i : furnitureIndices) {
            stmt = conn.prepareStatement("INSERT INTO orderitem (OrderID,ItemID) VALUES (?,?)");
            stmt.setInt(1,orderIndex);
            stmt.setInt(2, i+1);
            stmt.executeUpdate();
        }
    }
    public static Furniture[] findItems() {
        List<Furniture> listOfFurniture = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM items");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String type = rs.getString("Type");
                double length = rs.getDouble("Length");
                double width = rs.getDouble("Width");
                double height = rs.getDouble("Height");
                String materialType = rs.getString("MaterialType");
                String color = rs.getString("Color");
                Double price = rs.getDouble("Price");
                int numberOfDrawers = rs.getInt("NumberOfDrawers");
                Furniture item = null;
                    switch (type) {
                        case "bed" -> item = new Bed(materialType, color, price, length, width, height);
                        case "chair" -> item = new Chair(materialType, color, price, length, width, height);
                        case "dining table" -> item = new DiningTable(materialType, color, price, length, width, height);
                        case "sofa" -> item = new Sofa(materialType, color, price, length, width, height);
                        case "storage cabinet" -> {
                            item = new StorageCabinet(materialType, color, price, length, width, height, numberOfDrawers);
                        }
                        default -> System.out.println("Error: invalid item type found in database.");
                    }
                listOfFurniture.add(item);
            }
            stmt = conn.prepareStatement("SELECT ItemID, TableOfChair FROM items");
            rs = stmt.executeQuery();
            while (rs.next()) {
                if(rs.getInt("TableOfChair") != 0) {
                    ((DiningTable) listOfFurniture.get(rs.getInt("TableOfChair")-1)).listOfChairs.add((Chair)listOfFurniture.get(rs.getInt("ItemID")-1));
                }
            }
        } catch(SQLException ex) {}
        Furniture[] array = new Furniture[listOfFurniture.size()];
        
        return listOfFurniture.toArray(array);
    }

    public static Order[] findOrders() {
        List<Order> orders = new ArrayList<>();
        Customer customer;
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM orders");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Date dateOfOrder = rs.getDate("DateOfOrder");
                int customerID = rs.getInt("CustomerID");
                stmt = conn.prepareStatement("SELECT * FROM customers WHERE id = " + customerID);
                ResultSet rs2 = stmt.executeQuery();
                while (rs2.next()) {
                    String name = rs2.getString("name");
                    Date dob = rs2.getDate("dob");
                    int addressID = rs2.getInt("addressID");
                    stmt = conn.prepareStatement("SELECT * FROM address WHERE addressID = " + addressID);
                    ResultSet rs3 = stmt.executeQuery();
                    while (rs3.next()) {
                        List<Furniture> items = new ArrayList<>();
                        String street = rs3.getString("street");
                        String city = rs3.getString("city");
                        String state = rs3.getString("state");
                        String zip = rs3.getString("zip");
                        String address = street + ", " + city + ", " + state + ", " + zip;
                        customer = new Customer(customerID, name, dob, address);
                        stmt = conn.prepareStatement("SELECT * FROM orderitem WHERE OrderID = " + rs.getInt("OrderID"));
                        ResultSet rs4 = stmt.executeQuery();
                        while (rs4.next()) {
                            int ItemID = rs4.getInt("ItemID");
                            stmt = conn.prepareStatement("SELECT * FROM items WHERE ItemID = " + ItemID);
                            ResultSet rs5 = stmt.executeQuery();
                            while (rs5.next()) {
                                String type = rs5.getString("Type");
                                double length = rs5.getDouble("Length");
                                double width = rs5.getDouble("Width");
                                double height = rs5.getDouble("Height");
                                String materialType = rs5.getString("MaterialType");
                                String color = rs5.getString("Color");
                                Double price = rs5.getDouble("Price");
                                int numberOfDrawers = rs5.getInt("NumberOfDrawers");
                                Furniture item = null;
                                    switch (type) {
                                        case "bed" -> item = new Bed(materialType, color, price, length, width, height);
                                        case "chair" -> item = new Chair(materialType, color, price, length, width, height);
                                        case "dining table" -> item = new DiningTable(materialType, color, price, length, width, height);
                                        case "sofa" -> item = new Sofa(materialType, color, price, length, width, height);
                                        case "storage cabinet" -> {
                                            item = new StorageCabinet(materialType, color, price, length, width, height, numberOfDrawers);
                                        }
                                        default -> System.out.println("Error: invalid item type found in database.");
                                    }
                                items.add(item);
                            }
                        }
                            Furniture[] array = new Furniture[items.size()];
                            orders.add(new Order(customer, dateOfOrder, (Furniture[]) items.toArray(array)));
                    }
                }
            }
        } catch(SQLException ex) {}
        Order[] array = new Order[orders.size()];
        return orders.toArray(array);
    }
    
    public static String[] customers() {
        List<String> customer_names = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM customers");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                customer_names.add(rs.getString("name"));
        }
        } catch(SQLException e) {}
        String[] array = new String[customer_names.size()];
        return customer_names.toArray(array);
    }
    
    public List<Customer> readCustomers() throws SQLException {
        return readCustomers("");
    }
    public List<Customer> readCustomers(String nameQuery) throws SQLException {
        List<Customer> customers = new ArrayList<>();
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM customers WHERE name LIKE '%" + nameQuery + "%'");
        try (ResultSet rs = statement.executeQuery()){
            while (rs.next()) {
                PreparedStatement stmt = conn.prepareStatement("Select * FROM address WHERE addressID=" + rs.getInt("addressID"));
                ResultSet rs2 = stmt.executeQuery();
                String name = rs.getString("name");
                Date dob = rs.getDate("dob");
                int id = rs.getInt("id");
                while(rs2.next()) {
                    String street = rs2.getString("street");
                    String city = rs2.getString("city");
                    String state = rs2.getString("state");
                    String zip = rs2.getString("zip");
                    customers.add(new Customer(id, name, dob, street + ", " + city + ", " + state + ", " + zip));
                }
            }
        }
        return customers;
    }
    public void writeCustomer(Customer customer) throws SQLException {
        String[] address = customer.address().split(",");
        for (int i=0;i<address.length;i++)
            address[i] = address[i].trim();
        PreparedStatement statement = conn.prepareStatement("insert into address (street, city, state, zip) values (?,?,?,?)");
        for (int i=0;i<address.length;i++)
            statement.setString(i+1,address[i]);
        statement.executeUpdate();

        int addressID = getTableKey(
                "street","city","state","zip",
                address[0],address[1],address[2],address[3]);

        statement = conn.prepareStatement("INSERT INTO customers (name,dob,addressID) VALUES (?,?,?)");
        statement.setString(1, customer.name());
        statement.setDate(2, new Date(customer.dob().getTime()));
        statement.setInt(3,addressID);
        statement.executeUpdate();
    }

    public List<Furniture> readItems(String search) throws SQLException{
        Map<Integer,Furniture> itemMap = new HashMap<>();
        String[] fields = {"A.ItemID","A.Type","A.MaterialType","A.Color"};
        StringBuilder where = new StringBuilder(search.equals("") ? "" : (" WHERE "));
        for (int i=0;i<fields.length;i++){
            where.append(fields[i]).append(" >= ?");
            if (i!=fields.length-1)
                where.append(" OR ");
        }
        PreparedStatement statement = conn.prepareStatement("SELECT A.ItemID as a_id, A.Type as a_type, A.Length as a_length, A.Width as a_width, A.Height as a_height, " +
                "A.MaterialType as a_materialtype, A.Color as a_color, A.Price as a_price, A.NumberOfDrawers as numdrawers, " +
                "B.ItemID as b_id, B.Type as b_type, B.Length as b_length, B.Width as b_width, B.Height as b_height, " +
                "B.MaterialType as b_materialtype, B.Color as b_color, B.Price as b_price, B.TableOfChair as tableofchair " +
                " FROM items A INNER JOIN items B ON A.ItemID = B.TableOfChair " + where);
        for (int i=1;i<=fields.length;i++){
            statement.setString(i,search);
        }
        try (ResultSet rs = statement.executeQuery()){
            while (rs.next()){
                Integer id = rs.getInt("a_id");
                if (itemMap.get(id)==null){
                    String type = rs.getString("a_type");
                    double length = rs.getDouble("a_length");
                    double width = rs.getDouble("a_width");
                    double height = rs.getDouble("a_height");
                    String mat = rs.getString("a_materialtype");
                    String color = rs.getString("a_color");
                    double price = rs.getDouble("a_price");
                    Furniture item = null;
                    switch (type) {
                        case "bed" -> item = new Bed(mat, color, price, length, width, height);
                        case "chair" -> item = new Chair(mat, color, price, length, width, height);
                        case "dining table" -> item = new DiningTable(mat, color, price, length, width, height);
                        case "sofa" -> item = new Sofa(mat, color, price, length, width, height);
                        case "storage cabinet" -> {
                            int drawers = rs.getInt("numdrawers");
                            item = new StorageCabinet(mat, color, price, length, width, height, drawers);
                        }
                        default -> System.out.println("Error: invalid item type found in database.");
                    }

                    itemMap.put(id,item);
                } else {
                    if (itemMap.get(id) instanceof DiningTable dt){
                        String type = rs.getString("b_type");
                        double length = rs.getDouble("b_length");
                        double width = rs.getDouble("b_width");
                        double height = rs.getDouble("b_height");
                        String mat = rs.getString("b_materialtype");
                        String color = rs.getString("b_color");
                        double price = rs.getDouble("b_price");
                        if (type.equals("chair"))
                            dt.listOfChairs.add(new Chair(mat,color,price,length,width,height));
                        itemMap.put(id,dt);
                    }
                }
            }
        }

        return (List<Furniture>) itemMap.values();
    }
    public List<Furniture> readItems(String fieldName, String sQuery) throws SQLException {
        Map<Integer,Furniture> itemMap = new HashMap<>();
        String where = sQuery.equals("")?"":(" AND "+fieldName+" = ?");
        PreparedStatement statement = conn.prepareStatement("SELECT A.ItemID as a_id, A.Type as a_type, A.Length as a_length, A.Width as a_width, A.Height as a_height, " +
                "A.MaterialType as a_materialtype, A.Color as a_color, A.Price as a_price, A.NumberOfDrawers as numdrawers, " +
                "B.ItemID as b_id, B.Type as b_type, B.Length as b_length, B.Width as b_width, B.Height as b_height, " +
                "B.MaterialType as b_materialtype, B.Color as b_color, B.Price as b_price, B.TableOfChair as tableofchair " +
                " FROM items A INNER JOIN items B ON A.ItemID = B.TableOfChair " + where);
        if (!sQuery.equals(""))
            statement.setString(1,sQuery);
        try (ResultSet rs = statement.executeQuery()){
            while (rs.next()){
                Integer id = rs.getInt("a_id");
                if (itemMap.get(id)==null){
                    String type = rs.getString("a_type");
                    double length = rs.getDouble("a_length");
                    double width = rs.getDouble("a_width");
                    double height = rs.getDouble("a_height");
                    String mat = rs.getString("a_materialtype");
                    String color = rs.getString("a_color");
                    double price = rs.getDouble("a_price");
                    Furniture item = null;
                    switch (type) {
                        case "bed" -> item = new Bed(mat, color, price, length, width, height);
                        case "chair" -> item = new Chair(mat, color, price, length, width, height);
                        case "dining table" -> item = new DiningTable(mat, color, price, length, width, height);
                        case "sofa" -> item = new Sofa(mat, color, price, length, width, height);
                        case "storage cabinet" -> {
                            int drawers = rs.getInt("numdrawers");
                            item = new StorageCabinet(mat, color, price, length, width, height, drawers);
                        }
                        default -> System.out.println("Error: invalid item type found in database.");
                    }

                    itemMap.put(id,item);
                } else {
                    if (itemMap.get(id) instanceof DiningTable dt){
                        String type = rs.getString("b_type");
                        double length = rs.getDouble("b_length");
                        double width = rs.getDouble("b_width");
                        double height = rs.getDouble("b_height");
                        String mat = rs.getString("b_materialtype");
                        String color = rs.getString("b_color");
                        double price = rs.getDouble("b_price");
                        if (type.equals("chair"))
                            dt.listOfChairs.add(new Chair(mat,color,price,length,width,height));
                        itemMap.put(id,dt);
                    }
                }
            }
        }

        return itemMap.values().stream().toList();
    }
    public List<Furniture> readItems() throws SQLException {
        return readItems("","");
    }
    public static Chair[] findChairs() {
        ArrayList<Chair> listOfChairs = new ArrayList<>();
        try {

            var stmt = conn.prepareStatement("SELECT * FROM items WHERE Type='chair'");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                double length = rs.getDouble("Length");
                double width = rs.getDouble("Width");
                double height = rs.getDouble("Height");
                String mat = rs.getString("MaterialType");
                String color = rs.getString("Color");
                double price = rs.getDouble("Price");
                listOfChairs.add(new Chair(mat, color, price, length, width, height));
            }
        } catch (SQLException ex) {
        }
        Chair[] array = new Chair[listOfChairs.size()];
        array = listOfChairs.toArray(array);
        return array;
    }
    public void writeItem(Furniture item) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("insert into items (Type,Length,Width,Height,MaterialType,Color,Price,NumberOfDrawers) values (?,?,?,?,?,?,?,?)");
        statement.setString(1,item.getFurnitureType());
        statement.setDouble(2,item.dimensions.getLength());
        statement.setDouble(3,item.dimensions.getWidth());
        statement.setDouble(4,item.dimensions.getHeight());
        statement.setString(5,item.getMaterialType());
        statement.setString(6,item.getColor());
        statement.setDouble(7,item.getCost());
        if (item instanceof StorageCabinet sc) statement.setInt(8,sc.drawers);
        else statement.setInt(8,0);

        statement.executeUpdate();
        if (item instanceof DiningTable dt){
            statement = conn.prepareStatement("select * from items where Type= ? and Length= ? and Width= ?" +
                    " and Height= ? and MaterialType= ? and Color= ? and Price= ?");
            statement.setString(1,"dining table");
            statement.setDouble(2, dt.dimensions.getLength());
            statement.setDouble(3, dt.dimensions.getWidth());
            statement.setDouble(4, dt.dimensions.getHeight());
            statement.setString(5,dt.getMaterialType());
            statement.setString(6, dt.getColor());
            statement.setDouble(7,dt.getCost());
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    int tableID = rs.getInt("ItemID");
                    for (Chair c : dt.listOfChairs) {
                        statement = conn.prepareStatement("INSERT INTO items (Type,Length,Width,Height,MaterialType,Color,Price,TableOfChair) values (?,?,?,?,?,?,?,?)");
                        statement.setString(1, c.getFurnitureType());
                        statement.setDouble(2, c.dimensions.getLength());
                        statement.setDouble(3, c.dimensions.getWidth());
                        statement.setDouble(4, c.dimensions.getHeight());
                        statement.setString(5, c.getMaterialType());
                        statement.setString(6, c.getColor());
                        statement.setDouble(7, c.getCost());
                        statement.setInt(8, tableID);
                        statement.executeUpdate();
                    }
                }
            }
        }
    }

    // TODO: read/write orders
//    public List<Order> readOrders() throws SQLException{
//
//    }
//    public void writeOrder() throws SQLException {
//
//    }

    private int getTableKey(String... keyValues) throws SQLException {
        StringBuilder q = new StringBuilder("SELECT * FROM " + "address" + " WHERE ");
        for (int i = 0; i< 4; i++){
            q.append(keyValues[i]).append(" = ?");
            if (i!= 4 -1)
                q.append(" AND ");
        }
        PreparedStatement statement = conn.prepareStatement(q.toString());
        for (int i = 0; i< 4; i++){
            statement.setString(i+1,keyValues[i+ 4]);
        }
        try (ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("addressID");
            }
        }
        return -1;
    }
}
