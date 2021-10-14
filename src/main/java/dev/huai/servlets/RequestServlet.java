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


public class RequestServlet extends HttpServlet {

    private AuthService authService = new AuthService();
    private RequestServices requestServices = new RequestServices();
    private final Logger logger = Logger.getLogger(String.valueOf(RequestServlet.class));

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authToken = req.getHeader("Authorization");
        System.out.println("token is"+authToken);

        boolean tokenIsValidFormat = authService.validateToken(authToken);
        if (!tokenIsValidFormat) {
            resp.setStatus(400);
            logger.info("Invalid token");
        } else {
            User currentUser = authService.getUserByToken(authToken); // return null if none found

            if (currentUser == null) {
                resp.setStatus(401);
                logger.info("Token doesn't match database");
            } else {
                // if there is a valid token and that token is for an admin, return list of users
                if (currentUser.getUserRole() == UserRole.EMPLOYEE) {
                    //resp.setStatus(200);
                    Request request = new Request();
                    request.setDescription(req.getParameter("description"));
                    request.setUser_id(currentUser.getUser_id());
                    request.setBase64encodedString(req.getParameter("base64"));
                    System.out.println(request.toString());
                    Boolean result = requestServices.addRequest(request.getUser_id(), request.getDescription(), request.getBase64encodedString());
                    if (result == true) {
                        resp.setStatus(200);
                        logger.info("Adding request successfully");
                    } else {
                        resp.setStatus(403);
                        logger.info("Adding request failed");
                    }
                }else {
                    resp.setStatus(403);
                    logger.info("Token doesn't have priority level");
                }

            }

        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authToken = req.getHeader("Authorization");
        System.out.println("token is"+authToken);

        boolean tokenIsValidFormat = authService.validateToken(authToken);
        if (!tokenIsValidFormat) {
            resp.setStatus(400);
            logger.info("Invalid token");
        } else {
            User currentUser = authService.getUserByToken(authToken); // return null if none found

            if (currentUser == null) {
                resp.setStatus(401);
                logger.info("Token doesn't match database");
            } else {
                int request_id = Integer.parseInt(req.getHeader("Request"));
                //int request_id = Integer.parseInt(req.getParameter("request_id"));
                Request request = requestServices.getRequestByID(request_id);
                if(request.getUser_id()== currentUser.getUser_id() || currentUser.getUserRole()==UserRole.MANAGER){
                    try(PrintWriter pw = resp.getWriter()){
                        ObjectMapper om = new ObjectMapper();
                        System.out.println(request.getBase64encodedString());
                        String requestJson = om.writeValueAsString(request.getBase64encodedString());
                        pw.write(requestJson);
                    }
                    resp.setStatus(200);
                }
                else
                    // user token doesn't have authorization
                    resp.setStatus(401);
            }
        }
    }
}


    /*
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
                    System.out.println("I am a manager");
                    int request_id = Integer.parseInt(req.getParameter("request_id"));
                    String action = req.getParameter("action_type");
                    System.out.println("request_id is" + request_id+"action is " + action);
                    if(action.equals("deny")){
                        if(requestServices.denyRequestByID(request_id, currentUser.getUser_id()))
                            resp.setStatus(200);
                        else
                            System.out.println("action denied");
                    }
                    else if(action.equals("approve")){
                        if(requestServices.approveRequestByID(request_id, currentUser.getUser_id()))
                            resp.setStatus(200);
                        else
                            System.out.println("action denied");
                    }
                } else {
                    // if there is a valid token, but it's that of a general user rather than an admin, we could send back a 403
                    resp.sendError(403, "Invalid user role for current request");
                }
            }
        }
    }*/

