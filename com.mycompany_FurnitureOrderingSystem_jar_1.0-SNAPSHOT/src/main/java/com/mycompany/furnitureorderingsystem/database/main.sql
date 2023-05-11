CREATE TABLE address (
    addressID INT NOT NULL AUTO_INCREMENT,
    street VARCHAR(127),
    city VARCHAR(63),
    state CHAR(2),
    zip VARCHAR(16),
    primary key (addressID)
);
CREATE TABLE customers (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  dob DATE,
  addressID INT,
  PRIMARY KEY (id),
  foreign key (addressID) references address (addressID)
);
CREATE TABLE orders (
  OrderID INT NOT NULL AUTO_INCREMENT,
  DateOfOrder DATE NOT NULL,
  CustomerID INT NOT NULL,
  PRIMARY KEY (OrderID),
  foreign key (CustomerID) references customers (id)
);
CREATE TABLE items (
    ItemID INT NOT NULL AUTO_INCREMENT,
    Type VARCHAR(255) NOT NULL,
    Length DECIMAL(10,2),
    Width DECIMAL(10,2),
    Height DECIMAL(10,2),
    MaterialType VARCHAR(255),
    Color VARCHAR(255),
    Price DECIMAL(10,2),
    NumberOfDrawers INT,
    TableOfChair INT,
    PRIMARY KEY (ItemID),
    foreign key (TableOfChair) references items (ItemID)
);
create table orderItem(
    OrderID INT NOT NULL,
    ItemID INT NOT NULL,
    foreign key (OrderID) references orders (OrderID),
    foreign key (ItemID) references items (ItemID),
    primary key (OrderID, ItemID)
);
