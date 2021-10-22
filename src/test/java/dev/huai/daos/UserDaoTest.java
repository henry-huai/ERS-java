package dev.huai.daos;

import dev.huai.models.User;
import dev.huai.models.UserRole;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class UserDaoTest {
    UserDao userDao = new UserDaoImpl();

    @Test
    public void testGetUserByValidCredential() {
        User expected_user = new User();
        expected_user.setUser_id(1);
        expected_user.setFirstName("daniel");
        expected_user.setLastName("santiago");
        expected_user.setUserRole(UserRole.EMPLOYEE);
        expected_user.setEmail("daniel@gmail.com");
        User test_user = userDao.getUserByCredential(1, "1234");
        assertEquals(expected_user, test_user);
    }

    @Test
    public void testGetUserByInvalidCredential() {
        User test_user = userDao.getUserByCredential(1, "0000");
        assertNull(test_user);
    }


}
