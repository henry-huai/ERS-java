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
import java.util.logging.Logger;

public class ViewUserServlet extends HttpServlet {
    private AuthService authService = new AuthService();
    private UserServices userServices = new UserServices();
    private final Logger logger = Logger.getLogger(String.valueOf(RequestServlet.class));

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authToken = req.getHeader("Authorization");
        boolean tokenIsValidFormat = authService.validateToken(authToken);
        if(!tokenIsValidFormat){
            resp.setStatus(401);
            logger.info("Token invalid- no user matches");
        } else {
            User currentUser = authService.getUserByToken(authToken);

            if(currentUser==null){
                resp.setStatus(401);
                logger.info("Invalid token, no matched user");
            } else {
                if(currentUser.getUserRole() == UserRole.MANAGER){

                    try(PrintWriter pw = resp.getWriter()){
                        List<User> allEmployees = userServices.getAllEmployees();
                        ObjectMapper om = new ObjectMapper();
                        String requestJson = om.writeValueAsString(allEmployees);
                        pw.write(requestJson);
                        resp.setStatus(200);
                    }
                } else {
                    resp.setStatus(403);
                    logger.info("Token doesn't have priority level");
                }
            }

        }
    }
}
