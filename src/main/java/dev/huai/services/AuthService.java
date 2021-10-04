package dev.huai.services;

import dev.huai.models.User;
import dev.huai.models.UserRole;

public class AuthService {
    private UserServices userService = new UserServices();

    public boolean validateToken(String authToken){
        if(authToken==null){
            return false;
        }

        String[] tokenArr = authToken.split(":");
        if(tokenArr.length != 2){ // "a:b:c" "bananas"
            // we first check to see if our token has 2 values, separated by a colon
            // if it has more or less than 2, we return false
            return false;
        }

        // then we take a look at the first value, making sure it's numeric
        String idString = tokenArr[0];
        if(!idString.matches("^\\d+$")){ // if it does not match a number regular expression, we return false
            return false;
        }

        // then we look at the second value, making sure it matches a value in our enum
        String roleString = tokenArr[1];
        UserRole[] roles = UserRole.values(); // GENERAL, ADMIN
        for(UserRole role: roles){
            if(role.toString().equals(roleString)){ // check to see if the second value is one of our enum values
                return true;
            }
        }
        return false;
    }

    public User getUserByToken(String authToken){ // "2:admin"
        String[] tokenArr = authToken.split(":");
        int user_id = Integer.parseInt(tokenArr[0]);
        return userService.getUserByID(user_id);
    }
}
