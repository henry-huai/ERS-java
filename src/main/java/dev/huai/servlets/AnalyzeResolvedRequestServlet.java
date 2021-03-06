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
import java.util.ArrayList;
import java.util.logging.Logger;

public class AnalyzeResolvedRequestServlet extends HttpServlet {
    private AuthService authService = new AuthService();
    private RequestServices requestServices = new RequestServices();
    private final Logger logger = Logger.getLogger(String.valueOf(RequestServlet.class));

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authToken = req.getHeader("Authorization");

        boolean tokenIsValidFormat = authService.validateToken(authToken);
        if (!tokenIsValidFormat) {
            resp.setStatus(400);
            logger.info("Invalid token format");
        } else {
            User currentUser = authService.getUserByToken(authToken); // return null if none found

            if (currentUser == null) {
                resp.setStatus(401);
                logger.info("Token doesn't match database");
            } else {
                if(currentUser.getUserRole()==UserRole.MANAGER){

                    try(PrintWriter pw = resp.getWriter()){
                        ArrayList<Integer> arr = requestServices.getNumberOfResolveRequestByCategory();
                        ObjectMapper om = new ObjectMapper();
                        String requestJson = om.writeValueAsString(arr);
                        pw.write(requestJson);
                    }
                    resp.setStatus(200);
                    logger.info("Request analysis data returned");
                }
                else {
                    resp.setStatus(403);
                    logger.info("User doesn't have authorization level");
                }

            }
        }
    }
}
