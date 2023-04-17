package com.bridgelabz.EmployeePayroll;

import java.sql.*;

public class EmployeePayrollMain {
    public static void main(String[] args) {
        String url
                = "jdbc:mysql://localhost:3306/payroll_service";
        String username = "root";
        String password = "Admin";
        Connection con;
        String query
                = "select * from employee_payroll";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Driver name
            System.out.println("Driver Loaded !!!");
            System.out.println("Connecting to database" + url);
            con = DriverManager.getConnection(url, username, password);
            System.out.println("Connection Established successfully");
            Statement st = con.createStatement();
            boolean result = st.execute(query); // Execute query
            ResultSet rs = st.getResultSet();
            if(result) {
                while (rs.next()) {
                    int id = rs.getInt("id"); // Retrieve name from db
                    System.out.println(id); // Print result on console

                    String name = rs.getString("name"); // Retrieve name from db
                    System.out.println(name); // Print result on console

                    double salary
                            = rs.getDouble("salary"); // Retrieve name from db
                    System.out.println(salary); // Print result on console

                    String startDate
                            = rs.getString("start"); // Retrieve name from db
                    System.out.println(startDate); // Print result on console
                }
            } else {
                System.out.println("Invalid Query");
            }
            st.close(); // close statement
            con.close(); // close connection
            System.out.println("Connection Closed....");
        } catch (SQLException | ClassNotFoundException e){
            System.out.println("Catch block");
        }

    }
}
