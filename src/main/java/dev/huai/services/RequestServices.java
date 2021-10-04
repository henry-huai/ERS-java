package dev.huai.services;

import dev.huai.daos.RequestDao;
import dev.huai.daos.RequestDaoImpl;
import dev.huai.daos.UserDao;
import dev.huai.daos.UserDaoImpl;
import dev.huai.models.Request;
import dev.huai.models.User;

import java.util.ArrayList;
public class RequestServices {
    private RequestDao requestDao = new RequestDaoImpl();

    public ArrayList<Request> getPendingRequestsByID(Integer user_id){
        return requestDao.getPendingRequestsByID(user_id);
    }

    public ArrayList<Request> getResolvedRequestsByID(Integer user_id){
        return requestDao.getResolvedRequestsByID(user_id);
    }

    public boolean addRequest(Integer user_id, String description){
        return requestDao.addRequest(user_id, description);
    }

    public boolean approveRequestByID(Integer request_id, Integer user_id){
        return requestDao.approveRequestByID(request_id, user_id);
    }

    public boolean denyRequestByID(Integer request_id, Integer user_id){
        return requestDao.denyRequestByID(request_id, user_id);
    }

    public ArrayList<Request> getPendingRequestByManager(Integer user_id){
        return requestDao.getPendingRequestsByManager(user_id);
    }

    public ArrayList<Request> getResolvedRequestsByManager(Integer user_id){
        return requestDao.getResolvedRequestsByManager(user_id);
    }
}
