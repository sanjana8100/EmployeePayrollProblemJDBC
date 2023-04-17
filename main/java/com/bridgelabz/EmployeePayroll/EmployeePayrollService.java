package com.bridgelabz.EmployeePayroll;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.bridgelabz.EmployeePayroll.EmployeePayrollService.IOService.CONSOLE_IO;
import static com.bridgelabz.EmployeePayroll.EmployeePayrollService.IOService.DB_IO;

public class EmployeePayrollService {

    public enum IOService{
        FILE_IO,CONSOLE_IO,DB_IO,REST_IO
    }

    private List<EmployeePayrollData> employeePayrollList;

    private EmployeePayrollDBIOService employeePayrollDBIOService;

    public EmployeePayrollService() {
        employeePayrollDBIOService = EmployeePayrollDBIOService.getInstance();
    }

    public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList) {
        this();
        this.employeePayrollList = employeePayrollList;
    }

    public static void main(String[] args) {
        ArrayList<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        EmployeePayrollService employeePayrollService = new EmployeePayrollService(employeePayrollList);
        Scanner consoleInputReader = new Scanner(System.in);
        employeePayrollService.readEmployeePayrollData(CONSOLE_IO);
        employeePayrollService.writeEmployeePayrollData(CONSOLE_IO) ;
    }


    public void writeEmployeePayrollData(IOService ioService) {
        if(ioService.equals(CONSOLE_IO)){
            System.out.println("Writing Employee Payroll Data to Console\n " + employeePayrollList );
        } else if (ioService.equals(IOService.FILE_IO)) {
            new EmployeePayrollFileIOService().writeData(employeePayrollList);
        }
    }

    public void printEmployeePayrollData(IOService ioService){
        if (ioService.equals(IOService.FILE_IO)){
            new EmployeePayrollFileIOService().printData();
        }
    }

    public long countEntries(IOService ioService) {
        if (ioService.equals(IOService.FILE_IO)){
            return new EmployeePayrollFileIOService().countEntries();
        }
        return 0;
    }

    public List<EmployeePayrollData> readEmployeePayrollData(IOService ioService) {
        if (ioService.equals(DB_IO)){
            this.employeePayrollList = employeePayrollDBIOService.readData();
        }else if (ioService.equals(IOService.FILE_IO)) {
            this.employeePayrollList = new EmployeePayrollFileIOService().readData();
        }
        return employeePayrollList;
    }

    public void updateEmployeeSalary(String name, double salary) throws EmployeePayrollException {
        int result = employeePayrollDBIOService.updateEmployeeData(name,salary);
        if(result == 0) return;
        EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
        if (employeePayrollData != null){
            employeePayrollData.salary = salary;
        }
    }

    private EmployeePayrollData getEmployeePayrollData(String name) {
        return employeePayrollList.stream().filter(data -> data.name.equals(name)).findFirst().orElse(null);
    }

    public boolean checkEmployeePayrollInSyncWithDB(String name) {
        List<EmployeePayrollData> employeePayrollDataList = employeePayrollDBIOService.getEmployeePayrollData(name);
        return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
    }
}
