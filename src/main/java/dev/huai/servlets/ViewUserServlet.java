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
import java.util.List;

public class ViewUserServlet extends HttpServlet {
    private AuthService authService = new AuthService();
    private UserServices userServices = new UserServices();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authToken = req.getHeader("Authorization");
        System.out.println(authToken);

        boolean tokenIsValidFormat = authService.validateToken(authToken);
        if(!tokenIsValidFormat){
            resp.sendError(400, "Improper token format; cannot fulfill your request");
        } else {
            User currentUser = authService.getUserByToken(authToken); // return null if none found

            if(currentUser==null){
                resp.sendError(401, "Auth token invalid - no user");
            } else {
                // if there is a valid token and that token is for an admin, return list of users
                if(currentUser.getUserRole() == UserRole.MANAGER){

                    try(PrintWriter pw = resp.getWriter()){
                        List<User> allEmployees = userServices.getAllEmployees();
                        ObjectMapper om = new ObjectMapper();
                        String requestJson = om.writeValueAsString(allEmployees);
                        pw.write(requestJson);
                        resp.setStatus(200);
                    }
                } else {
                    // if there is a valid token, but it's that of a general user rather than an admin, we could send back a 403
                    resp.sendError(403, "Invalid user role for current request");
                }
            }

        }
    }
}
