package dev.huai.servlets;

import dev.huai.models.User;
import dev.huai.services.UserServices;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthServlet extends HttpServlet {
    private UserServices userServices = new UserServices();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int user_idParam = Integer.parseInt(req.getParameter("user_id"));
        String passwordParam = req.getParameter("password");

        System.out.println("Credentials received: "+user_idParam +" "+passwordParam);

        User user = userServices.getUserByCredentials(user_idParam, passwordParam);

        if(user == null){
            resp.sendError(401, "User credentials provided did not return a valid account");
        } else {
            // send 200 (OK) if we do find a user with those credentials
            resp.setStatus(200);
            // we can also send back some token that identifies the particular user that matched
            String token = user.getUser_id() + ":" + user.getUserRole();
            resp.setHeader("Authorization", token);
        }

    }
}
