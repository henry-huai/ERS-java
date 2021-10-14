package dev.huai.daos;

import dev.huai.models.Request;
import dev.huai.models.User;
import dev.huai.services.ConnectionService;
import dev.huai.services.EmailService;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Logger;

public class RequestDaoImpl implements RequestDao{
    private ConnectionService connectionService = new ConnectionService();
    private EmailService emailService = new EmailService();
    private UserDao userDao = new UserDaoImpl();
    //private final Logger logger = Logger.getLogger(String.valueOf(RequestDaoImpl.class));


    @Override
    public ArrayList<Request> getPendingRequestsByID(Integer user_id){
        return getPendingRequestsByIDBySQL(user_id);
    }

    @Override
    public ArrayList<Request> getResolvedRequestsByID(Integer user_id){
        return getResolvedRequestsByIDBySQL(user_id);
    }

    @Override
    public ArrayList<Request> getPendingRequestsByManager(Integer user_id) {
        return getPendingRequestsByManagerSQL(user_id);
    }

    @Override
    public ArrayList<Request> getResolvedRequestsByManager(Integer user_id) {
        return getResolvedRequestsByManagerSQL(user_id);
    }

    @Override
    public boolean addRequest(Integer user_id, String description, String base64) {
        return addRequestBySQL(user_id, description, base64);
    }

    @Override
    public Request getRequestByID(Integer request_id) {
        return getRequestByIDBySQL(request_id);
    }

    @Override
    public boolean approveRequestByID(Integer request_id, Integer user_id) {
        return approveRequestByIDBySQL(request_id, user_id);
    }

    @Override
    public boolean denyRequestByID(Integer request_id, Integer user_id) {
        return denyRequestByIDBySQL(request_id, user_id);
    }


    private ArrayList<Request> getPendingRequestsByIDBySQL(Integer user_id){
        String sql = " select * from requests where user_id = ? and status = 0";
        ArrayList<Request> pendingRequests = new ArrayList<>();


        try {
            Connection c = connectionService.establishConnection();
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setInt(1, user_id);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                Request request = new Request();
                request.setRequest_id(rs.getInt("request_id"));
                request.setUser_id(rs.getInt("user_id"));
                request.setDescription(rs.getString("description"));
                request.setStatus(rs.getInt("status"));
                request.setBase64encodedString((rs.getString("image")));
                request.setTransaction_date(rs.getString("transaction_date"));
                pendingRequests.add(request);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        //logger.info("Pending requests returned from database");
        return pendingRequests;

    }

    private ArrayList<Request> getResolvedRequestsByIDBySQL(Integer user_id){
        String sql = "select * from requests where user_id = ? and status != 0";
        ArrayList<Request> resolvedRequests = new ArrayList<>();


        try {
            Connection c = connectionService.establishConnection();
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setInt(1, user_id);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                Request request = new Request();
                request.setRequest_id(rs.getInt("request_id"));
                request.setUser_id(rs.getInt("user_id"));
                request.setDescription(rs.getString("description"));
                request.setStatus(rs.getInt("status"));
                request.setBase64encodedString((rs.getString("image")));
                request.setTransaction_date(rs.getString("transaction_date"));
                resolvedRequests.add(request);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        //logger.info("Resolved requests returned from database");
        return resolvedRequests;
    }

    private boolean addRequestBySQL(Integer user_id, String description, String base64){
        String sql = "insert into requests (user_id, description, image) values(?, ?, ?)";

        try{
            Connection c = connectionService.establishConnection();
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setInt(1, user_id);
            stmt.setString(2, description);
            stmt.setString(3,base64);
            stmt.execute();
            //logger.info("New request is added to database");
            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //logger.info("Adding new request failed");
        return false;
    }

    private Request getRequestByIDBySQL(Integer request_id){
        String sql = "select * from requests where request_id = ?";

        try{
            Connection c = connectionService.establishConnection();
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setInt(1, request_id);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Request request = new Request();
                request.setRequest_id(rs.getInt("request_id"));
                request.setUser_id(rs.getInt("user_id"));
                request.setDescription(rs.getString("description"));
                request.setBase64encodedString(rs.getString("image"));
                request.setTransaction_date(rs.getString("transaction_date"));
                request.setStatus(0);
                return request;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;

    }

    private ArrayList<Request> getPendingRequestsByManagerSQL(Integer user_id){
        String sql = " select * from requests where status = 0";
        ArrayList<Request> pendingRequests = new ArrayList<>();


        try {
            Connection c = connectionService.establishConnection();
            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                Request request = new Request();
                request.setRequest_id(rs.getInt("request_id"));
                request.setUser_id(rs.getInt("user_id"));
                request.setDescription(rs.getString("description"));
                request.setBase64encodedString(rs.getString("image"));
                request.setTransaction_date(rs.getString("transaction_date"));
                request.setStatus(0);
                pendingRequests.add(request);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        //logger.info("Pending requests by manager returned from database");
        return pendingRequests;
    }

    private ArrayList<Request> getResolvedRequestsByManagerSQL(Integer user_id){
        String sql = " select * from requests where status != 0";
        ArrayList<Request> resolvedRequests = new ArrayList<>();

        try {
            Connection c = connectionService.establishConnection();
            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                Request request = new Request();
                request.setRequest_id(rs.getInt("request_id"));
                request.setUser_id(rs.getInt("user_id"));
                request.setDescription(rs.getString("description"));
                request.setStatus(rs.getInt("status"));
                request.setBase64encodedString(rs.getString("image"));
                request.setTransaction_date(rs.getString("transaction_date"));
                resolvedRequests.add(request);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        //logger.info("Resolved requests by manager returned from database");
        return resolvedRequests;
    }

    private boolean approveRequestByIDBySQL(Integer request_id, Integer user_id){
        String sql = "update requests set status = ? where request_id = ?";

        try {
            Connection c = connectionService.establishConnection();
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setInt(1,user_id);
            stmt.setInt(2,request_id);
            stmt.execute();
            //logger.info("Approve request status successfully");
            Request request = getRequestByID(request_id);
            User user = userDao.getUserByID(request.getUser_id());
            String str1 = "  Request description: " + request.getDescription();
            String str2 = "Your request has been approved. Request ID: "+request.getRequest_id();
            String message = str2 + str1;
            emailService.sendEmail(user.getEmail(), message, "Request Update");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //logger.info("Approve request status failed");
        return false;
    }

    private boolean denyRequestByIDBySQL(Integer request_id, Integer user_id){
        String sql = "update requests set status = -? where request_id = ?";

        try {
            Connection c = connectionService.establishConnection();
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setInt(1,user_id);
            stmt.setInt(2,request_id);
            stmt.execute();
            //logger.info("Deny request status successfully");
            Request request = getRequestByID(request_id);
            User user = userDao.getUserByID(request.getUser_id());
            String str1 = "  Request description: " + request.getDescription();
            String str2 = "Your request has been denied. Request ID: "+request.getRequest_id();
            String message = str2 + str1;
            emailService.sendEmail(user.getEmail(), message, "Request Update");

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //logger.info("Deny request status failed");
        return false;
    }


}
