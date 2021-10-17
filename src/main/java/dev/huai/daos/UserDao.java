package dev.huai.daos;

import dev.huai.models.Request;
import dev.huai.models.User;

import java.util.ArrayList;
import java.util.List;

public interface UserDao {
    public User getUserByCredential(Integer user_id, String password);

    public User getUserByID(Integer user_id);

    public List<User> getAllEmployees();

    public boolean addNewEmployee(User employee);

    public boolean updatePassword(Integer user_id, String password, String newPassword);


}
