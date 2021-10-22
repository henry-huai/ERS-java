package dev.huai.servlets;

import dev.huai.models.User;
import dev.huai.models.UserRole;
import dev.huai.services.AuthService;
import dev.huai.services.RequestServices;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

public class UpdatingRequestServlet extends HttpServlet {
    private AuthService authService = new AuthService();
    private RequestServices requestServices = new RequestServices();
    private final Logger logger = Logger.getLogger(String.valueOf(RequestServlet.class));

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authToken = req.getHeader("Authorization");

        boolean tokenIsValidFormat = authService.validateToken(authToken);
        if(!tokenIsValidFormat){
            resp.setStatus(400);
            logger.info("Invalid token format");
        } else {
            User currentUser = authService.getUserByToken(authToken);

            if(currentUser==null){
                resp.setStatus(401);
                logger.info("Invalid token-no user matches");
            } else {
                if(currentUser.getUserRole() == UserRole.MANAGER){
                    int request_id = Integer.parseInt(req.getParameter("request_id"));
                    String action = req.getParameter("action_type");
                    if(action.equals("deny")){
                        if(requestServices.denyRequestByID(request_id, currentUser.getUser_id())) {
                            resp.setStatus(200);
                            logger.info("Request succeed");
                        }
                        else{
                            resp.setStatus(402);
                            logger.info("request failed in database");
                        }
                    }
                    else if(action.equals("approve")){
                        if(requestServices.approveRequestByID(request_id, currentUser.getUser_id())) {
                            resp.setStatus(200);
                            logger.info("Request succeed");
                        }
                        else{
                            resp.setStatus(402);
                            logger.info("request failed in database");
                        }
                    }
                } else {
                    resp.setStatus(403);
                    logger.info("Token doesn't have priority level");
                }
            }
        }
    }

}
