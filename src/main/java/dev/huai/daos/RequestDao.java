package dev.huai.daos;

import dev.huai.models.Request;

import java.util.ArrayList;

public interface RequestDao {
    public ArrayList<Request> getPendingRequestsByID(Integer user_id);

    public ArrayList<Request> getResolvedRequestsByID(Integer user_id);

    public ArrayList<Request> getPendingRequestsByManager(Integer user_id);

    public ArrayList<Request> getResolvedRequestsByManager(Integer user_id);

    public boolean addRequest(Integer user_id, String description, String category);

    public Request getRequestByID(Integer request_id);

    public boolean approveRequestByID(Integer request_id, Integer user_id);

    public boolean denyRequestByID(Integer request_id, Integer user_id);

    public int getNumberOfPendRequestByCategory(String category);

    public ArrayList<Integer> getNumberOfResolveRequestByCategory();
}
