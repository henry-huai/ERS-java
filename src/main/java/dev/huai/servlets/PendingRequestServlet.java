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
import java.util.logging.Logger;

public class PendingRequestServlet extends HttpServlet {

    private AuthService authService = new AuthService();
    private RequestServices requestServices = new RequestServices();
    private final Logger logger = Logger.getLogger(String.valueOf(RequestServlet.class));

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authToken = req.getHeader("Authorization");
        boolean tokenIsValidFormat = authService.validateToken(authToken);
        if(!tokenIsValidFormat){
            resp.setStatus(400);
            logger.info("Improper token format; cannot fulfill your request");
        } else {
            User currentUser = authService.getUserByToken(authToken);

            if(currentUser==null){
                resp.setStatus(401);
                logger.info("Invalid token, no matched user");
            } else {
                if(currentUser.getUserRole() == UserRole.EMPLOYEE){
                    resp.setStatus(200);
                    logger.info("List returned");
                    try(PrintWriter pw = resp.getWriter()){
                        List<Request> pendingRequests = requestServices.getPendingRequestsByID(currentUser.getUser_id());
                        ObjectMapper om = new ObjectMapper();
                        String requestJson = om.writeValueAsString(pendingRequests);
                        pw.write(requestJson);
                    }
                } else if(currentUser.getUserRole() == UserRole.MANAGER){
                    resp.setStatus(200);
                    logger.info("List returned");
                    try(PrintWriter pw = resp.getWriter()) {
                        List<Request> pendingRequests = requestServices.getPendingRequestsByManager(currentUser.getUser_id());
                        ObjectMapper om = new ObjectMapper();
                        String requestJson = om.writeValueAsString(pendingRequests);
                        pw.write(requestJson);
                    }
                }
                else {
                    resp.setStatus(403);
                    logger.info("User doesn't have authorization role");
                }
            }
        }
    }
}
