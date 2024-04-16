package Midteam;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Define interface
interface IEmployee {
    void showInfo();
}

// Define Employee class
class Employee implements IEmployee, Serializable {
    private static final long serialVersionUID = 1L;
    protected String ID;
    protected String fullName;
    protected String birthday;
    protected String phone;
    protected String email;
    protected String employeeType;
    protected static int employeeCount = 0;

    public Employee(String ID, String fullName, String birthday, String phone, String email, String employeeType) {
        this.ID = ID;
        this.fullName = fullName;
        this.birthday = birthday;
        this.phone = phone;
        this.email = email;
        this.employeeType = employeeType;
        employeeCount++;
    }

    public void showInfo() {
        System.out.println("ID: " + ID);
        System.out.println("Full Name: " + fullName);
        System.out.println("Birthday: " + birthday);
        System.out.println("Phone: " + phone);
        System.out.println("Email: " + email);
        System.out.println("Employee Type: " + employeeType);
    }
}

class Experience extends Employee {
    private int expInYear;
    private String proSkill;

    public Experience(String ID, String fullName, String birthday, String phone, String email, String employeeType, int expInYear, String proSkill) {
        super(ID, fullName, birthday, phone, email, employeeType);
        this.expInYear = expInYear;
        this.proSkill = proSkill;
    }

    @Override
    public void showInfo() {
        super.showInfo();
        System.out.println("Experience in Years: " + expInYear);
        System.out.println("Professional Skill: " + proSkill);
    }
}


class Fresher extends Employee {
    private String graduationDate;
    private String graduationRank;
    private String education;

    public Fresher(String ID, String fullName, String birthday, String phone, String email, String employeeType, String graduationDate, String graduationRank, String education) {
        super(ID, fullName, birthday, phone, email, employeeType);
        this.graduationDate = graduationDate;
        this.graduationRank = graduationRank;
        this.education = education;
    }

    @Override
    public void showInfo() {
        super.showInfo();
        System.out.println("Graduation Date: " + graduationDate);
        System.out.println("Graduation Rank: " + graduationRank);
        System.out.println("Education: " + education);
    }
}

class Intern extends Employee {
    private String majors;
    private String semester;
    private String universityName;

    public Intern(String ID, String fullName, String birthday, String phone, String email, String employeeType, String majors, String semester, String universityName) {
        super(ID, fullName, birthday, phone, email, employeeType);
        this.majors = majors;
        this.semester = semester;
        this.universityName = universityName;
    }

    @Override
    public void showInfo() {
        super.showInfo();
        System.out.println("Majors: " + majors);
        System.out.println("Semester: " + semester);
        System.out.println("University Name: " + universityName);
    }
}


class EmployeeManagement {
    private List<Employee> employees = new ArrayList<>();

    // Add employee
    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

 
    public Employee getEmployeeByID(String ID) {
        for (Employee emp : employees) {
            if (emp.ID.equals(ID)) {
                return emp;
            }
        }
        return null;
    }

   
    public void updateEmployee(Employee updatedEmployee) {
        for (int i = 0; i < employees.size(); i++) {
            Employee emp = employees.get(i);
            if (emp.ID.equals(updatedEmployee.ID)) {
                employees.set(i, updatedEmployee);
                break;
            }
        }
    }

   
    public void deleteEmployee(String ID) {
        employees.removeIf(emp -> emp.ID.equals(ID));
    }

   
    public void displayEmployees() {
        for (Employee emp : employees) {
            emp.showInfo();
          System.out.println("..........");
        }
    }


    public void writeToFile(String filename) {
        try {
            FileOutputStream fileOut = new FileOutputStream(filename);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(employees);
            objectOut.close();
            fileOut.close();
            System.out.println("Employees have been written to the file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFromFile(String filename) {
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            employees = (List<Employee>) objectIn.readObject();
            objectIn.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

  
    public void writeToDatabase() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/employee", "root", "password");
            for (Employee emp : employees) {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO employees (ID, fullName, birthday, phone, email, employeeType) VALUES (?, ?, ?, ?, ?, ?)");
                stmt.setString(1, emp.ID);
                stmt.setString(2, emp.fullName);
                stmt.setString(3, emp.birthday);
                stmt.setString(4, emp.phone);
                stmt.setString(5, emp.email);
                stmt.setString(6, emp.employeeType);
                stmt.executeUpdate();
            }
            conn.close();
            System.out.println("Employees have been written to the database successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void readFromDatabase() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/employee", "root", "");
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM employees");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String ID = rs.getString("ID");
                String fullName = rs.getString("fullName");
                String birthday = rs.getString("birthday");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                String employeeType = rs.getString("employeeType");
            
                if (employeeType.equals("Experience")) {
                    int expInYear = rs.getInt("expInYear");
                    String proSkill = rs.getString("proSkill");
                    Experience emp = new Experience(ID, fullName, birthday, phone, email, employeeType, expInYear, proSkill);
                    employees.add(emp);
                } else if (employeeType.equals("Fresher")) {
                    String graduationDate = rs.getString("graduationDate");
                    String graduationRank = rs.getString("graduationRank");
                    String education = rs.getString("education");
                    Fresher emp = new Fresher(ID, fullName, birthday, phone, email, employeeType, graduationDate, graduationRank, education);
                    employees.add(emp);
                } else if (employeeType.equals("Intern")) {
                    String majors = rs.getString("majors");
                    String semester = rs.getString("semester");
                    String universityName = rs.getString("universityName");
                    Intern emp = new Intern(ID, fullName, birthday, phone, email, employeeType, majors, semester, universityName);
                    employees.add(emp);
                }
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


public class Main {
    public static void main(String[] args) {
     
        EmployeeManagement empManagement = new EmployeeManagement();

        Experience emp1 = new Experience("1" ,"Mai Truong Thinh", "26-04-2005", "057276868", "thinhmt23.@vku.udn.vn", "Experience", 5, "Java");
        Fresher emp2 = new Fresher("2", "pham hong dang", "1995-05-05", "083876721", "hongdanggmail.com", "Fresher", "2022-05-30", "Excellent", "University duytan");
        Intern emp3 = new Intern("3", "mai sy hung", "2000-10-10", "03757265", "hung123gmail.com", "Intern", "Computer Science", "Spring 2024", "Đông Á University");

       
        empManagement.addEmployee(emp1);
        empManagement.addEmployee(emp2);
        empManagement.addEmployee(emp3);

       
        System.out.println("Employees:");
        empManagement.displayEmployees();

        empManagement.writeToFile("employees.dat");

       
        System.out.println("\nEmployees after reading from file:");
        empManagement.readFromFile("employees.dat");
        empManagement.displayEmployees();

     
        Experience updatedEmp1 = new Experience("1", "Mai Truong Thinh", "26-04-2005", "057276868", "thinhmt23.@vku.udn.vn", "Experience", 6, "Java, Python");
        empManagement.updateEmployee(updatedEmp1);

        empManagement.deleteEmployee("003");

      
        System.out.println("\nEmployees after update and delete:");
        empManagement.displayEmployees();

        empManagement.writeToDatabase();

        System.out.println("\nEmployees after reading from database:");
        empManagement.readFromDatabase();
        empManagement.displayEmployees();
    }
}
