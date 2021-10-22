package dev.huai.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.huai.models.Request;
import dev.huai.models.User;
import dev.huai.models.UserRole;
import dev.huai.services.AuthService;
import dev.huai.services.RequestServices;
import dev.huai.services.UserServices;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;

public class NewEmployeeServlet extends HttpServlet {

    private AuthService authService = new AuthService();
    private UserServices userServices = new UserServices();
    private final Logger logger = Logger.getLogger(String.valueOf(RequestServlet.class));

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authToken = req.getHeader("Authorization");
        boolean tokenIsValidFormat = authService.validateToken(authToken);
        if(!tokenIsValidFormat){
            resp.setStatus(400);
            logger.info("Improper token format; cannot fulfill your request");
        } else {
            User currentUser = authService.getUserByToken(authToken); // return null if none found

            if(currentUser==null){
                resp.setStatus(401);
                logger.info("Auth token invalid - no user");
            } else {
                // if there is a valid token and that token is for an admin, return list of users
                if(currentUser.getUserRole() == UserRole.MANAGER){
                    User newEmployee = new User();
                    newEmployee.setFirstName(req.getParameter("firstName"));
                    newEmployee.setLastName(req.getParameter("lastName"));
                    newEmployee.setEmail(req.getParameter("email"));
                    if(userServices.addNewEmployee(newEmployee)){
                        resp.setStatus(200);
                        logger.info("New user added");
                    }
                    else {
                        resp.setStatus(403);
                        logger.info("Adding new employee failed");
                    }
                } else {
                    // if there is a valid token, but it's that of a general user rather than an admin, we could send back a 403
                    resp.setStatus(403);
                    logger.info("Auth token invalid - no authorization");
                }
            }
        }
    }
}
