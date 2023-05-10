package com.mycompany.furnitureorderingsystem.database;

import com.mycompany.furnitureorderingsystem.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLConnection {
    public static SQLConnection instance;
    private final Connection conn;
    public SQLConnection(String url, String username, String password) throws SQLException {
        conn = DriverManager.getConnection(url, username, password);
        instance = this;
    }

    public List<Customer> readCustomers() throws SQLException {
        return readCustomers("");
    }
    public List<Customer> readCustomers(String nameQuery) throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String where = nameQuery.equals("")?"":(" AND customers.name >= '" + nameQuery + "'");
        PreparedStatement statement = conn.prepareStatement("SELECT customers.id as customer_id," +
                "customers.name as customer_name," +
                "customers.dob as customer_dob," +
                "address.street as address_street," +
                "address.city as address_city," +
                "address.state as address_state," +
                "address.zip as address_zip " +
                "FROM customers " +
                "LEFT JOIN address " +
                "ON customers.addressID = address.addressID" +
                where);
        try (ResultSet rs = statement.executeQuery()){
            // Process the result set
            while (rs.next()) {
                String street = rs.getString("address_street");
                String city = rs.getString("address_city");
                String state = rs.getString("address_state");
                String zip = rs.getString("address_zip");
                String name = rs.getString("customer_name");
                Date dob = rs.getDate("customer_dob");
                int id = rs.getInt("customer_id");
                customers.add(new Customer(id, name, dob, street + ", " + city + ", " + state + ", " + zip));
            }
        }
        return customers;
    }
    public void writeCustomer(Customer customer) throws SQLException {
        String[] address = customer.address().split(",");
        for (int i=0;i<address.length;i++)
            address[i] = address[i].trim();
        address[0] = address[0].substring(0,126>=address[0].length()?address[0].length()-1:126);
        address[1] = address[1].substring(0,62>=address[1].length()?address[1].length()-1:62);
        address[2] = address[2].substring(0,1>=address[2].length()?address[2].length()-1:1);
        address[3] = address[3].substring(0,15>=address[3].length()?address[3].length()-1:15);

        // TODO: adjust this to match address class
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
    public void writeItem(Furniture item) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("insert into items (Type,Length,Width,Height,MaterialType,Color,Price,NumberOfDrawers) values (?,?,?,?,?,?,?)");
        statement.setString(1,item.getFurnitureType());
        statement.setDouble(2,item.dimensions.getLength());
        statement.setDouble(3,item.dimensions.getWidth());
        statement.setDouble(4,item.dimensions.getHeight());
        statement.setString(5,item.getMaterialType());
        statement.setString(6,item.getColor());
        statement.setDouble(7,item.getCost());
        if (item instanceof StorageCabinet sc) statement.setInt(8,sc.drawers);

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
