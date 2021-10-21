package dev.huai.services;

import dev.huai.daos.UserDao;
import dev.huai.daos.UserDaoImpl;
import dev.huai.models.User;

import java.util.List;

public class UserServices {

    private UserDao userDao = new UserDaoImpl();

    public User getUserByCredentials(Integer user_id, String password){
        if(user_id == null || password == null || password.isEmpty()){
            return null;
        }
        return userDao.getUserByCredential(user_id, password);
    }

    public User getUserByID(Integer user_id){
        return userDao.getUserByID(user_id);
    }

    public List<User> getAllEmployees(){
        return userDao.getAllEmployees();
    }

    public boolean addNewEmployee(User user){
        return userDao.addNewEmployee(user);
    }

    public boolean updatePassword(Integer user_id, String oldPassword, String newPassword){
        return userDao.updatePassword(user_id, oldPassword, newPassword);
    }

}
