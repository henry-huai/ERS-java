package dev.huai.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;

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
import java.util.logging.Logger;


public class AuthServlet extends HttpServlet {
    private UserServices userServices = new UserServices();
    private AuthService authService = new AuthService();
    private final Logger logger = Logger.getLogger(String.valueOf(RequestServlet.class));

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int user_idParam = Integer.parseInt(req.getParameter("user_id"));
        String passwordParam = req.getParameter("password");

        System.out.println("Credentials received: "+user_idParam +" "+passwordParam);

        User user = userServices.getUserByCredentials(user_idParam, passwordParam);
        if(user == null){
            resp.setStatus(403);
            logger.info("Invalid login credential");
            resp.setHeader("Location", "http://localhost:8081/project1/static/login.html");
        } else {
            resp.setStatus(200);
            logger.info("Log in successfully");
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
            resp.setStatus(400);
            logger.info("Invalid token format");
        } else {
            User currentUser = authService.getUserByToken(authToken); // return null if none found

            if (currentUser == null) {
                resp.setStatus(401);
                logger.info("Auth token invalid - no user");

            } else {
                // if there is a valid token and that token is for an admin, return list of users
                resp.setStatus(200);
                try (PrintWriter pw = resp.getWriter()) {
                    User user = userServices.getUserByID(currentUser.getUser_id());
                    ObjectMapper om = new ObjectMapper();
                    String requestJson = om.writeValueAsString(user);
                    pw.write(requestJson);
                    logger.info("User information returned");
                    // write to response body
                }
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authToken = req.getHeader("Authorization");
        String oldPassword = req.getParameter("oldPassword");
        String newPassword = req.getParameter("newPassword");

        boolean tokenIsValidFormat = authService.validateToken(authToken);
        if(!tokenIsValidFormat){
            resp.setStatus(400);
            logger.info("Invalid token format");
        } else {
            User currentUser = authService.getUserByToken(authToken); // return null if none found

            if (currentUser == null) {
                resp.setStatus(401);
                logger.info("Auth token invalid - no user");

            } else {
                // if there is a valid token and that token is for an admin, return list of users
                resp.setStatus(200);
                try (PrintWriter pw = resp.getWriter()) {
                    User user = userServices.getUserByID(currentUser.getUser_id());
                    ObjectMapper om = new ObjectMapper();
                    String requestJson = om.writeValueAsString(user);
                    pw.write(requestJson);
                    logger.info("User information returned");
                    // write to response body
                }
            }
        }
    }
}



