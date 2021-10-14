package dev.huai.daos;

import dev.huai.models.User;
import dev.huai.models.UserRole;
import dev.huai.services.ConnectionService;
import dev.huai.services.EmailService;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Logger;

public class UserDaoImpl implements UserDao {
    private ConnectionService connectionService = new ConnectionService();
    private EmailService emailService = new EmailService();
    //private final Logger logger = Logger.getLogger(String.valueOf(UserDaoImpl.class));

    @Override
    public User getUserByCredential(Integer user_id, String password) {
        return getUserByCredentialBySQL(user_id, password);
    }
    @Override
    public User getUserByID(Integer user_id){
        return getUserByIDBySQL(user_id);
    }

    @Override
    public List<User> getAllEmployees() {
        return getAllEmployeesBySQL();
    }

    @Override
    public boolean addNewEmployee(User employee) {
        return addNewEmployeeBySQL(employee);
    }


    private User getUserByCredentialBySQL(Integer user_id, String password){
        String sql1 = "select * from users where user_id =?";
        String sql2 = "select * from users where user_id =? and pass_word = ?";
        User user = new User();

        try{
            Connection c = connectionService.establishConnection();
            PreparedStatement stmt1 = c.prepareStatement(sql1);
            stmt1.setInt(1, user_id);
            ResultSet rs1 = stmt1.executeQuery();

            if(rs1.next()){
                PreparedStatement stmt2 = c.prepareStatement(sql2);
                stmt2.setInt(1, user_id);
                stmt2.setString(2, password);

                //store the return value user_id from database
                ResultSet rs = stmt2.executeQuery();
                if(rs.next()) {
                    user.setUser_id(rs.getInt("user_id"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setEmail(rs.getString("email"));
                    if(rs.getBoolean("ismanager")){
                        user.setUserRole(UserRole.MANAGER);
                    }
                    else
                        user.setUserRole(UserRole.EMPLOYEE);
                    //logger.info("User found from database");
                }
                else{
                    //logger.info("Password doesn't match the user ID");
                    return null;
                }
            }
            else{
                //logger.info("User ID not found in database");
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    private User getUserByIDBySQL(Integer user_id){
        String sql = "select * from users where user_id = ?";
        User user = new User();

        try{
            Connection c = connectionService.establishConnection();
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setInt(1, user_id);

            //store the return value user_id from database
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                user.setUser_id(user_id);
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                if(rs.getBoolean("ismanager")){
                    user.setUserRole(UserRole.MANAGER);
                }
                else
                    user.setUserRole(UserRole.EMPLOYEE);
                //logger.info("User found from database");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    private List<User> getAllEmployeesBySQL(){
        String sql = "select * from users where ismanager = false";
        List<User> allEmployees = new ArrayList<>();

        try{
            Connection c = connectionService.establishConnection();
            PreparedStatement stmt = c.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                User employee = new User();
                employee.setUser_id(rs.getInt("user_id"));
                employee.setFirstName(rs.getString("first_name"));
                employee.setLastName(rs.getString("last_name"));
                employee.setEmail(rs.getString("email"));
                employee.setUserRole(UserRole.EMPLOYEE);
                allEmployees.add(employee);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //logger.info("Employees returned from database");
        return allEmployees;

    }

    private boolean addNewEmployeeBySQL(User employee){
        String sql="insert into users (first_name, last_name, pass_word, email) values (?, ?, ?, ?) returning user_id";

        try {
            Connection c = connectionService.establishConnection();
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1,employee.getFirstName());
            stmt.setString(2,employee.getLastName());
            String password = String.copyValueOf(generatePassword(8));
            stmt.setString(3,password);
            stmt.setString(4, employee.getEmail());
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                String id = Integer.toString(rs.getInt("user_id"));
                String str1 = " password: " + password;
                String str2 = " user id: " + id;
                String result = str1 + str2;
                emailService.sendEmail(employee.getEmail(), result);
            }
            //logger.info("New Employee has been added, email sent");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    private static char[] generatePassword(int length) {
        String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String specialCharacters = "!@#$";
        String numbers = "1234567890";
        String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
        Random random = new Random();
        char[] password = new char[length];

        password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
        password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
        password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
        password[3] = numbers.charAt(random.nextInt(numbers.length()));

        for(int i = 4; i< length ; i++) {
            password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        return password;
    }

}
