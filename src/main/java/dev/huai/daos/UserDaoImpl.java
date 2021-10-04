package dev.huai.daos;

import dev.huai.models.Request;
import dev.huai.models.User;
import dev.huai.models.UserRole;
import dev.huai.services.ConnectionService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private ConnectionService connectionService = new ConnectionService();

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
                    System.out.println("user has been returned");
                }
                else{
                    System.out.println("Password doesn't match the user ID");
                    return null;
                }
            }
            else{
                System.out.println("User ID not found");
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
                allEmployees.add(employee);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return allEmployees;

    }


}
