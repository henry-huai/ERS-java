package dev.huai.daos;

import dev.huai.models.Request;
import dev.huai.services.ConnectionService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RequestDaoImpl implements RequestDao{
    private ConnectionService connectionService = new ConnectionService();

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
    public boolean addRequest(Integer user_id, String description) {
        return addRequestBySQL(user_id, description);
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
                pendingRequests.add(request);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                resolvedRequests.add(request);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resolvedRequests;
    }

    private boolean addRequestBySQL(Integer user_id, String description){
        String sql = "insert into requests (user_id, description) values(?, ?)";
        try{
            Connection c = connectionService.establishConnection();
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setInt(1, user_id);
            stmt.setString(2, description);
            stmt.execute();
            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    private ArrayList<Request> getPendingRequestsByManagerSQL(Integer user_id){
        String sql = " select * from requests where status = 0";
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
                request.setStatus(0);
                pendingRequests.add(request);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pendingRequests;
    }

    private ArrayList<Request> getResolvedRequestsByManagerSQL(Integer user_id){
        String sql = " select * from requests where status != 0";
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
                resolvedRequests.add(request);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
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
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
