package com.mycompany.furnitureorderingsystem.database;

import com.mycompany.furnitureorderingsystem.*;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        String where = nameQuery.equals("")?"":(" WHERE customers.name like '%" + nameQuery + "%'");
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
                customers.add(new Customer(id, name, dob, new Address(street,city,state,zip)));
            }
        }
        return customers;
    }

    private static String trimAddress(String part, int size){
        return part.trim().substring(0, Math.min(size, part.length()));
    }
    public void writeCustomer(Customer customer) throws SQLException {
        Address address = customer.address();
        String street = trimAddress(address.street(), 127);
        String city = trimAddress(address.city(), 63);
        String state = trimAddress(address.state(), 2);
        String zip = trimAddress(address.zip(), 16);

        PreparedStatement statement = conn.prepareStatement("insert into address (street, city, state, zip) values (?,?,?,?)");
        statement.setString(1,street);
        statement.setString(2,city);
        statement.setString(3,state);
        statement.setString(4,zip);
        statement.executeUpdate();

        int addressID = getAddressKey(address);

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
        if (search.equals("")) fields = new String[0];
        for (int i=0;i<fields.length;i++){
            where.append(fields[i]).append(" like ?");
            if (i!=fields.length-1)
                where.append(" OR ");
        }
        PreparedStatement statement = conn.prepareStatement("SELECT A.ItemID as a_id, A.Type as a_type, A.Length as a_length, A.Width as a_width, A.Height as a_height, " +
                "A.MaterialType as a_materialtype, A.Color as a_color, A.Price as a_price, A.NumberOfDrawers as numdrawers, " +
                "B.ItemID as b_id, B.Type as b_type, B.Length as b_length, B.Width as b_width, B.Height as b_height, " +
                "B.MaterialType as b_materialtype, B.Color as b_color, B.Price as b_price, B.TableOfChair as tableofchair " +
                " FROM items A LEFT JOIN items B ON A.ItemID = B.TableOfChair " + where);
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

        return itemMap.values().stream().toList();
    }
    public List<Furniture> readItems(String fieldName, String sQuery) throws SQLException {
        Map<Integer,Furniture> itemMap = new HashMap<>();
        String where = sQuery.equals("")?"":(" WHERE "+fieldName+" = ?");
        PreparedStatement statement = conn.prepareStatement("SELECT A.ItemID as a_id, A.Type as a_type, A.Length as a_length, A.Width as a_width, A.Height as a_height, " +
                "A.MaterialType as a_materialtype, A.Color as a_color, A.Price as a_price, A.NumberOfDrawers as numdrawers, " +
                "B.ItemID as b_id, B.Type as b_type, B.Length as b_length, B.Width as b_width, B.Height as b_height, " +
                "B.MaterialType as b_materialtype, B.Color as b_color, B.Price as b_price, B.TableOfChair as tableofchair " +
                " FROM items A LEFT JOIN items B ON A.ItemID = B.TableOfChair " + where);
        if (!sQuery.equals(""))
            statement.setString(1,sQuery);
        try (ResultSet rs = statement.executeQuery()){
            while (rs.next()){
                Integer id = rs.getInt("a_id");
                if (!itemMap.containsKey(id)){
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

    public List<Order> readOrders(String search) throws SQLException{
        Map<Integer,Order> orderMap = new HashMap<>();
        Map<Integer,Furniture> itemMap = new HashMap<>();
        Map<Integer,Customer> customerMap = new HashMap<>();
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        List<String> fields = new ArrayList<>();
        Integer orderID = null;
        Date orderData = null;
        Double orderCost = null;
        try {
            orderID = Integer.parseInt(search);
            fields.add("orders.OrderID");
        } catch (NumberFormatException ignored){}
        try {
            orderData = new Date(format.parse(search).getTime());
            fields.add("orders.DateOfOrder");
        } catch (ParseException ignored) {}
        try {
            orderCost = Double.parseDouble(search);
            fields.add("orders.TotalCost");
        } catch (NumberFormatException ignored){}

        StringBuilder where = new StringBuilder(search.equals("") ? "" : " AND (");
        for (int i=0;i<fields.size();i++){
            where.append(fields.get(i)).append(" like ?");
            if (i!=fields.size()-1)
                where.append(" OR ");
            else
                where.append(")");
        }
        PreparedStatement statement = conn.prepareStatement(
                "select O.OrderID as id, \n" +
                        "O.DateOfOrder as date, \n" +
                        "Cu.id as cu_id,\n" +
                        "Cu.name as cu_name,\n" +
                        "Cu.dob as cu_dob,\n" +
                        "A.street as street,\n" +
                        "A.city as city,\n" +
                        "A.state as state,\n" +
                        "A.zip as zip,\n" +
                        "I.ItemID as i_id,\n" +
                        "I.Type as type,\n" +
                        "I.Length as length,\n" +
                        "I.Width as width,\n" +
                        "I.Height as height,\n" +
                        "I.Price as cost,\n" +
                        "I.MaterialType as matType,\n" +
                        "I.Color as color,\n" +
                        "I.NumberOfDrawers as drawers,\n" +
                        "C.ItemID as c_id,\n" +
                        "C.Type as c_type,\n" +
                        "C.Length as c_length,\n" +
                        "C.Width as c_width,\n" +
                        "C.Height as c_height,\n" +
                        "C.MaterialType as c_matType,\n" +
                        "C.Price as c_cost,\n" +
                        "C.Color as c_color\n" +
                        "from orders as O\n" +
                        "left join orderitem as OI\n" +
                        "on O.OrderID = OI.OrderID\n" +
                        "left join items as I\n" +
                        "on OI.ItemID = I.ItemID\n" +
                        "left join items as C\n" +
                        "on I.ItemID = C.TableOfChair\n" +
                        "left join customers as Cu\n" +
                        "on O.CustomerID = Cu.id\n" +
                        "left join address as A\n" +
                        "on Cu.addressID = A.addressID" + where);
        int index = 1;
        if (orderID!=null)
            statement.setInt(index++,orderID);
        if (orderData!=null)
            statement.setDate(index++,orderData);
        if (orderCost!=null)
            statement.setDouble(index,orderCost);

        try (ResultSet rs = statement.executeQuery()){
            while (rs.next()){
                int id = rs.getInt("id");
                Order order;
                if (!orderMap.containsKey(id)) {
                    int customerID = rs.getInt("cu_id");
                    Customer customer;
                    if (!customerMap.containsKey(customerID)){
                        String name = rs.getString("cu_name");
                        Date dob = rs.getDate("cu_dob");
                        String street = rs.getString("street");
                        String city = rs.getString("city");
                        String state = rs.getString("state");
                        String zip = rs.getString("zip");
                        customer = new Customer(customerID,name,dob,new Address(street,city,state,zip));
                        customerMap.put(customerID,customer);
                    } else {
                        customer = customerMap.get(customerID);
                    }

                    Date date = rs.getDate("date");

                    order = new Order(customer,date,new ArrayList<>());
                } else {
                    order = orderMap.get(id);
                }
                int iID = rs.getInt("i_id");
                Furniture item;
                if (!itemMap.containsKey(iID)) {
                    double cost = rs.getDouble("cost");
                    String type = rs.getString("type");
                    double length = rs.getDouble("length");
                    double width = rs.getDouble("width");
                    double height = rs.getDouble("height");
                    String matType = rs.getString("matType");
                    String color = rs.getString("color");
                    int drawers = rs.getInt("drawers");
                    item = Furniture.getFurniture(type, matType, color, cost, length, width, height, drawers);
                    itemMap.put(iID, item);
                } else {
                    item = itemMap.get(iID);
                }
                int cID = rs.getInt("c_id");
                if (item instanceof DiningTable dt)
                    if (!itemMap.containsKey(cID)){
                        double cost = rs.getDouble("c_cost");
                        String type = rs.getString("c_type");
                        double length = rs.getDouble("c_length");
                        double width = rs.getDouble("c_width");
                        double height = rs.getDouble("c_height");
                        String matType = rs.getString("c_matType");
                        String color = rs.getString("c_color");
                        Furniture chair = Furniture.getFurniture(type, matType, color, cost, length, width, height, 0);
                        itemMap.put(cID, chair);
                        dt.listOfChairs.add((Chair) chair);
                    } else {
                        dt.listOfChairs.add((Chair) itemMap.get(cID));
                    }
                order.addItem(item);
                orderMap.put(id,order);
            }
        }
        return orderMap.values().stream().toList();
    }

    public void writeOrder(Order order) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("insert into orders (DateOfOrder,CustomerID,TotalCost) Values (?,?,?)");
        Date date = new Date(order.getDate().getTime());
        statement.setDate(1,date);
        Customer customer = order.getCustomer();
        int customerKey = getCustomerKey(customer);
        statement.setInt(2, customerKey);
        statement.setDouble(3,order.getTotalCost());
        statement.executeUpdate();

        statement = conn.prepareStatement("select OrderID from orders where DateOfOrder= ? and CustomerID= ? and TotalCost= ?");
        statement.setDate(1,date);
        statement.setInt(2,customerKey);
        statement.setDouble(3, order.getTotalCost());
        int orderID;
        try (ResultSet rs = statement.executeQuery()){
            orderID = rs.getInt("OrderID");
        }

        for (Furniture item: order.getItems()){
            int key = getItemKey(item);
            statement = conn.prepareStatement("insert into orderitem (OrderID, ItemID) Values (?,?)");
            statement.setInt(1,orderID);
            statement.setInt(2,key);
            statement.executeUpdate();
        }
    }

    private int getItemKey(Furniture furniture) throws SQLException{
        PreparedStatement statement = conn.prepareStatement("select ItemID from items where Type= ? and Length= ? and Width= ? " +
                "and Height= ? and MaterialType= ? and Color= ? and Price= ?"/* and NumberOfDrawers= ?"*/);
        statement.setString(1,furniture.getFurnitureType());
        statement.setDouble(2,furniture.dimensions.getLength());
        statement.setDouble(3,furniture.dimensions.getWidth());
        statement.setDouble(4,furniture.dimensions.getHeight());
        statement.setString(5,furniture.getMaterialType());
        statement.setString(6, furniture.getColor());
        statement.setDouble(7,furniture.getCost());
        /*if (furniture instanceof StorageCabinet sc)
            statement.setInt(8,sc.drawers);
        else
            statement.setInt(8,0);*/

        try (ResultSet rs = statement.executeQuery()){
            rs.next();
            return rs.getInt("ItemID");
        }
    }

    private int getCustomerKey(Customer customer) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("select id from customers where name= ? and dob= ?");
        statement.setString(1, customer.name());
        statement.setDate(2,new Date(customer.dob().getTime()));
        try (ResultSet rs = statement.executeQuery()){
            rs.next();
            return rs.getInt("id");
        }
    }

    private int getAddressKey(Address address) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM address WHERE street= ? and city= ? and state= ? and zip= ?");

        String street = trimAddress(address.street(), 127);
        String city = trimAddress(address.city(), 63);
        String state = trimAddress(address.state(), 2);
        String zip = trimAddress(address.zip(), 16);
        statement.setString(1,street);
        statement.setString(2,city);
        statement.setString(3,state);
        statement.setString(4,zip);

        try (ResultSet rs = statement.executeQuery()) {
            rs.next();
            return rs.getInt("addressID");
        }
    }
}
