package com.bridgelabz.EmployeePayroll;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollDBIOService {
    private PreparedStatement employeePayrollDataStatement;
    private static EmployeePayrollDBIOService employeePayrollDBIOService;

    private EmployeePayrollDBIOService() {
    }

    public static EmployeePayrollDBIOService getInstance(){
        if(employeePayrollDBIOService == null){
            employeePayrollDBIOService = new EmployeePayrollDBIOService();
        }
        return employeePayrollDBIOService;
    }

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/payroll_service";
        String username = "root";
        String password = "Romil@33";
        Connection con;
        System.out.println("Connecting to database" + url);
        con = DriverManager.getConnection(url, username, password);
        System.out.println("Connection Established successfully");
        return con;
    }

    public List<EmployeePayrollData> readData() {
        String query = "select * from employee_payroll";
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try {
            Connection connection = this.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            employeePayrollList = this.getEmployeePayrollData(resultSet);
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return employeePayrollList;
    }

    public int updateEmployeeData(String name, double salary) throws EmployeePayrollException {
        return this.updateEmployeeDataUsingStatement(name,salary);
    }

    private int updateEmployeeDataUsingStatement(String name, double salary) throws EmployeePayrollException {
        String query = String.format("Update employee_payroll set salary = %.2f where name = '%s';",salary,name);
        try (Connection connection = this.getConnection();){
            Statement statement = connection.createStatement();
            return statement.executeUpdate(query);
        } catch (SQLException e){
            throw new EmployeePayrollException("Failed to update!!!");
        }
    }


    public List<EmployeePayrollData> getEmployeePayrollData(String name) {
        List<EmployeePayrollData> employeePayrollList = null;
        if(this.employeePayrollDataStatement == null){
            this.prepareStatementForEmployeeData();
        }
        try {
            employeePayrollDataStatement.setString(1,name);
            ResultSet resultSet = employeePayrollDataStatement.executeQuery();
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try {
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                LocalDate startDate = resultSet.getDate("start").toLocalDate();
                employeePayrollList.add(new EmployeePayrollData(id,name,salary,startDate));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    private void prepareStatementForEmployeeData() {
        try {
            Connection connection = this.getConnection();
            String sql = "Select * from employee_payroll where name = ?";
            employeePayrollDataStatement = connection.prepareStatement(sql);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
