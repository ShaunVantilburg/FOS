# FOS
Indiana Tech – Talwar College of Engineering and Computer Sciences
School of Computer Sciences
Spring - 2023
CS 3700 – Object Orientation
Project Title: Furniture Ordering System

Team:
Shaun Van Tilburg
Josh Carter
Mauricio Salvador Espinosa Torres
Leila Garcia Amaral Certo
Jordan Alvin Highsmith

Instructions:
You will work in a team of 5 together to design a Furniture Ordering System (FOS) that store,
retrieve, edit, and delete information regarding furniture, customers, and orders. You need to
provide the following:

1- Zip file for your Java project.

    a. If you are using NetBeans: make sure that your project is open in NetBeans (the
    only open project), click on File -> Export Project -> To ZIP … to generate one zip
    file.

    b. If you are using a different IDE: put all java classes in one single zip file.

2- Documentation pdf file: this file has the following sections:

    a. Introduction: briefly describe the purpose of the project.

    b. Architecture: provide a UML class diagram showing a big picture of your project.
    Use the Lucidchart, visit (https://www.lucidchart.com/pages/) then click on Sign
    up free and select the free version and sign up. Next, you can start the online
    editor and create UML class diagram for your project. You can save and come
    back later to work on it. Once you finished all the work, click on File -> Export ->
    PDF and download the file to submit.

    c. MySQL Database: describes the structure of your database like tables and fields
    used along with connection information like username and password.

    d. GUI: describe how to use the GUI. There are two ways to do that:

        i. Describe the purpose of every GUI component, like this button do this and
        this window do that and so on.

        ii. Provide scenarios or use-cases. For example, adding a new item scenario,
        and you start by asking the user to click on add new item button then a
        window will open, and you have to enter information on this field and that
        field and so on till the user finish this task. Then you explain another
        scenario like searching for an item and so on.

    e. Testing: describe how you used Unit testing to check for errors in your project.
    For example, you can state that you design the insert unit testing for existing item
    to see if the function for adding new books that exists in the library return back
    with an error.

Rubrics:
1- 20% for UML class diagrams (each student must contribute with at least one class).
2- 40% for Java classes design (each student must contribute with at least one class).
3- 10% for Java GUI (it is possible that one student takes care of this task).
4- 10% for database files (it is possible that one student takes care of this task).
5- 20% for documentation and testing (all students must contribute to this section).

Details:
Design a Furniture Ordering System (FPS) that consists of three main
parts:
Part-1: Classes design
Part-2: GUI design
Part-3: Main class and database connectivity
Part-1: Consider the following when designing your classes.
1- Each class should be in a separate file.
2- Attributes with * are complex ones, in other words, there need to be
implemented as classes and used with composition.
3- There are lots of repetitions, so you need to use inheritance by
using more classes.
Hint: all classes share dimensions, material type, color, and price ,
so these can be put on a separate class and all other classes inherit
these attributes.
4- Protect some attributes by making them private and provide setters
and getters methods. You need to confirm with me after deciding what
you will do, and this is through the design report.

class Customer:
Name
*DOB
*Address

class Order:
*Customer
*Date of order
*List of items (List of ordered furniture items)
Total price

class Chair:
*Dimensions
Material type
Color
Price

class Sofa:
*Dimensions
Material type
Color
Price

class Dining Table:
*Dimensions
Material type
Color
*List of chairs
Price

class Bed:
*Dimensions
Material type
Color
Price

class Storage Cabinet:
*Dimensions
Material type
Color
Number of drawers
Price

Part-2: design a GUI that support the following operations:
4
1- Add a new customer.
2- Add a new item.
3- Create an order.
4- Search for a customer and display information.
5- Search for an item and display information.
6- Search for an order and display information.
7- List all orders.
Part-3: Design the Main class to use your GUI and the classes you designed
for the project. You also need to use MySQL database to save data about
FOS.
