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


public class RequestServlet extends HttpServlet {

    private AuthService authService = new AuthService();
    private RequestServices requestServices = new RequestServices();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
                    //resp.setStatus(200);
                    Request request = new Request();
                    request.setDescription(req.getParameter("description"));
                    request.setUser_id(currentUser.getUser_id());
                    if(requestServices.addRequest(request.getUser_id(), request.getDescription()))
                        resp.setStatus(200);
                    else
                        resp.sendError(400, "Unknown error");
                } else {
                    // if there is a valid token, but it's that of a general user rather than an admin, we could send back a 403
                    resp.sendError(403, "Invalid user role for current request");
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
}
