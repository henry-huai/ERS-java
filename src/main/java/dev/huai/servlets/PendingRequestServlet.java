package dev.huai.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.huai.models.Request;
import dev.huai.models.User;
import dev.huai.models.UserRole;
import dev.huai.services.AuthService;
import dev.huai.services.RequestServices;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class PendingRequestServlet extends HttpServlet {

    private AuthService authService = new AuthService();
    private RequestServices requestServices = new RequestServices();

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
                if(currentUser.getUserRole() == UserRole.EMPLOYEE){
                    resp.setStatus(200);

                    try(PrintWriter pw = resp.getWriter()){
                        List<Request> pendingRequests = requestServices.getPendingRequestsByID(currentUser.getUser_id());
                        ObjectMapper om = new ObjectMapper();
                        String requestJson = om.writeValueAsString(pendingRequests);
                        pw.write(requestJson);
                        // write to response body
                    }
                } else if(currentUser.getUserRole() == UserRole.MANAGER){
                    resp.setStatus(200);

                    try(PrintWriter pw = resp.getWriter()) {
                        List<Request> pendingRequests = requestServices.getPendingRequestsByManager(currentUser.getUser_id());
                        ObjectMapper om = new ObjectMapper();
                        String requestJson = om.writeValueAsString(pendingRequests);
                        pw.write(requestJson);
                    }
                }
                else {
                    // if there is a valid token, but it's that of a general user rather than an admin, we could send back a 403
                    resp.sendError(403, "Invalid user role for current request");
                }
            }
        }
    }
}
