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

public class ResolvedRequestServlet extends HttpServlet {

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
                logger.info("Token invalid- no user matches");
            } else {
                if(currentUser.getUserRole() == UserRole.EMPLOYEE){
                    resp.setStatus(200);

                    try(PrintWriter pw = resp.getWriter()){
                        List<Request> pendingRequests = requestServices.getResolvedRequestsByID(currentUser.getUser_id());
                        ObjectMapper om = new ObjectMapper();
                        String requestJson = om.writeValueAsString(pendingRequests);
                        pw.write(requestJson);
                    }
                    logger.info("List returned");
                } else if(currentUser.getUserRole() == UserRole.MANAGER){
                    resp.setStatus(200);

                    try(PrintWriter pw = resp.getWriter()) {
                        List<Request> pendingRequests = requestServices.getResolvedRequestsByManager(currentUser.getUser_id());
                        ObjectMapper om = new ObjectMapper();
                        String requestJson = om.writeValueAsString(pendingRequests);
                        pw.write(requestJson);
                    }
                    logger.info("List returned");
                }else {
                    resp.setStatus(403);
                    logger.info("Invalid user role, no authorization");
                }
            }
        }
    }
}
