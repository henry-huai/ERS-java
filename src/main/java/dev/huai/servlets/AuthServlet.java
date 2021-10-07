package dev.huai.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.huai.models.Request;
import dev.huai.models.User;
import dev.huai.models.UserRole;
import dev.huai.services.AuthService;
import dev.huai.services.UserServices;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class AuthServlet extends HttpServlet {
    private UserServices userServices = new UserServices();
    private AuthService authService = new AuthService();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int user_idParam = Integer.parseInt(req.getParameter("user_id"));
        String passwordParam = req.getParameter("password");

        System.out.println("Credentials received: "+user_idParam +" "+passwordParam);

        User user = userServices.getUserByCredentials(user_idParam, passwordParam);
        if(user == null){
            resp.setHeader("Location", "http://localhost:8081/project1/static/login.html");
        } else {
            resp.setStatus(200);
            String token = user.getUser_id() + ":" + user.getUserRole();
            if(user.getUserRole().equals(UserRole.MANAGER))
                resp.setHeader("Location", "http://localhost:8081/project1/static/manager.html");
            else
                resp.setHeader("Location", "http://localhost:8081/project1/static/employee.html");
            resp.setHeader("Authorization", token);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authToken = req.getHeader("Authorization");
        System.out.println(authToken);

        boolean tokenIsValidFormat = authService.validateToken(authToken);
        if(!tokenIsValidFormat){
            resp.sendError(400, "Improper token format; cannot fulfill your request");
        } else {
            User currentUser = authService.getUserByToken(authToken); // return null if none found

            if (currentUser == null) {
                resp.sendError(401, "Auth token invalid - no user");
            } else {
                // if there is a valid token and that token is for an admin, return list of users
                resp.setStatus(200);
                try (PrintWriter pw = resp.getWriter()) {
                    User user = userServices.getUserByID(currentUser.getUser_id());
                    ObjectMapper om = new ObjectMapper();
                    String requestJson = om.writeValueAsString(user);
                    pw.write(requestJson);
                    // write to response body
                }
            }
        }
    }
}



