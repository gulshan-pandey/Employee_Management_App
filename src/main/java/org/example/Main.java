package org.example;


import java.sql.*;
import java.util.Scanner;

public class Main {

    public static final String URL = "jdbc:mysql://localhost:3306/employee";
    public static final String USER = "root" ;
    public static final String PASSWORD= "1234" ;

    private static Connection connection ;

    private static PreparedStatement prepstmt;

    private static ResultSet resultset;

    private static Scanner sc = new Scanner(System.in);



    public static void main( String[] args ) {

        try {
            connection = DriverManager.getConnection(URL,USER,PASSWORD);

            System.out.println("Welcome to the Employee Management System \n");
            createEmpTable();

            System.out.println("1. Insert Data\n2. Fetch Data\n3. Update Data\n4. Delete Data\n5. Exit\n");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    insertData();
                    break;
                case 2:
                    fetchData();

                    break;
                case 3:
                    updateEmpData();
                    break;
                case 4:
                    deleteEmpData();
                    break;
                case 5:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid Choice");
            }


        } catch (Exception e) {
            System.out.println( "ERROR occured:: " + e.getMessage());
        }


    }



    private static void deleteEmpData() throws SQLException {

        fetchData();        //fetch data before deleting
        System.out.println("Enter the number of Employee you want to delete");
        int row=0;
        try {
            row = sc.nextInt();
        } catch (Exception e) {
            System.out.println("Error::" + " you entered wrong input " +  e.getMessage());
        }
        String query = " DELETE FROM Employee WHERE id =?";
        prepstmt = connection.prepareStatement(query);

        for (int i = 0; i < row; i++) {
            System.out.println("Enter the Employee ID you want to delete");
            int id = 0;
            try {
                id = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Error::" + " you entered wrong input " +  e.getMessage());
            }

            prepstmt.setInt(1, id);
            int deletedRows = prepstmt.executeUpdate();
            if (deletedRows > 0) {
                System.out.println("Data for Employee ID " + id + " deleted successfully!");
            } else {
                System.out.println("No employee found with ID " + id + " to delete.");
            }
        }

    }



    private static void updateEmpData() throws SQLException {
        fetchData();
        System.out.println("Enter the number of Employee you want to update");
        int rows=0;
        try {
             rows = sc.nextInt();
        } catch (Exception e) {
            System.out.println("Error::" + " you entered wrong input " +  e.getMessage());
        }

        for (int i = 0; i < rows; i++) {
            System.out.println("Enter the Employee ID you want to update");
            int id = 0;
            try {
                id = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Error::" + " you entered wrong input " +  e.getMessage());
            }
            System.out.println("Enter the new Details of the Employee");
            System.out.println("\nEnter the name of the Employee");
            String name = sc.next();
            System.out.println(" Enter the age of the Employee");
            int age = sc.nextInt();
            System.out.println(" Enter the email of the Employee");
            String email = sc.next();

            String query = "UPDATE Employee SET name =?, age =?, email =? WHERE id =?";

            prepstmt = connection.prepareStatement(query);
            prepstmt.setString(1,name);
            prepstmt.setInt(2,age);
            prepstmt.setString(3,email);
            prepstmt.setInt(4,id);

            int updatedRows = prepstmt.executeUpdate();
            if (updatedRows > 0) {
                System.out.println("Data for Employee ID " + id + " updated successfully!");
            } else {
                System.out.println("No employee found with ID " + id + " to update.");
            }


        }
    }




    private static void fetchData() throws SQLException {

        String query = "select * from  Employee";
        prepstmt = connection.prepareStatement((query));
        resultset  = prepstmt.executeQuery();
        System.out.println("-------------------------- EMPLOYEE DETAILS --------------------------");
        while(resultset.next()){
            System.out.println("Employee ID - "+ resultset.getInt("id"));
            System.out.println("Employee name - "+ resultset.getString("name"));
            System.out.println("Employee age - "+ resultset.getInt("age"));
            System.out.println("Employee email - " + resultset.getString("email"));
            System.out.println("================================================================");
        }
    }



    private static void insertData() throws SQLException {
        System.out.println("Enter the number of Employees you want to insert: ");
        int rows = 0;
        try {
            rows = sc.nextInt();
            sc.nextLine(); // Consume newline left-over
        } catch (Exception e) {
            System.out.println("Error: You entered wrong input. " + e.getMessage());
            return;
        }

        String query = "INSERT INTO Employee (name, age, email) VALUES(?,?,?)";
        prepstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);             // for auto increment id

        for (int i = 0; i < rows; i++) {
            System.out.println("\n Enter the details of Employee " + (i + 1));

            System.out.println("Enter the name of the Employee");
            String name = sc.nextLine();
            System.out.println("Enter the age of the Employee");
            int age = sc.nextInt();
            sc.nextLine(); // Consume newline left-over
            System.out.println("Enter the email of the Employee");
            String email = sc.nextLine();

            prepstmt.setString(1, name);
            prepstmt.setInt(2, age);
            prepstmt.setString(3, email);
            prepstmt.addBatch();
        }

        int[] rowsInserted = prepstmt.executeBatch();
        ResultSet generatedKeys = prepstmt.getGeneratedKeys();
        if (generatedKeys != null) {
            int i = 0;
            while (generatedKeys.next()) {
                long generatedId = generatedKeys.getLong(1);
                System.out.println("Generated ID for Employee " + (i + 1) + ": " + generatedId);
                i++;
            }
        }

        for (int i = 0; i < rowsInserted.length; i++) {
            if (rowsInserted[i] == 1) {
                System.out.println("Data inserted successfully for Employee " + (i + 1));
            } else {
                System.out.println("Data not inserted for Employee " + (i + 1));
            }
        }
    }


    private static void createEmpTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS Employee(id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(100), " +
                "age INT ," +
                "email VARCHAR(100) UNIQUE)";

        prepstmt = connection.prepareStatement(query);
        prepstmt.execute();
        System.out.println("Table created successfully!...");
    }



}




