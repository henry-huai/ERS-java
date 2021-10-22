package dev.huai.daos;

import dev.huai.models.Request;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class RequestDaoTest {
    RequestDao requestDao = new RequestDaoImpl();

    @Test
    public void testAddRequest() {
        assertEquals(true, requestDao.addRequest(1, "test", "BUSINESS"));
    }

    @Test
    public void testAddInvalidRequest() {
        assertEquals(false, requestDao.addRequest(-1, "test", "BUSINESS"));
    }

    @Test
    public void testGetRequestByValidID() {
        Request expected_request = new Request();
        expected_request.setRequest_id(5);
        expected_request.setCategory("BUSINESS");
        expected_request.setDescription("test");
        expected_request.setStatus(0);
        expected_request.setUser_id(1);
        expected_request.setTransaction_date("2021-10-21");
        assertEquals(expected_request, requestDao.getRequestByID(5));
    }

    @Test
    public void testGetRequestByInvalidID() {
        assertNull(requestDao.getRequestByID(-1));
    }
}