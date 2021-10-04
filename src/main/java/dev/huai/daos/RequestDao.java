package dev.huai.daos;

import dev.huai.models.Request;

import java.util.ArrayList;

public interface RequestDao {
    public ArrayList<Request> getPendingRequestsByID(Integer user_id);

    public ArrayList<Request> getResolvedRequestsByID(Integer user_id);

    public ArrayList<Request> getPendingRequestsByManager(Integer user_id);

    public ArrayList<Request> getResolvedRequestsByManager(Integer user_id);

    public boolean addRequest(Integer user_id, String description);

    public boolean approveRequestByID(Integer request_id, Integer user_id);

    public boolean denyRequestByID(Integer request_id, Integer user_id);
}
