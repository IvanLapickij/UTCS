package Members;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MembersLoginBeanTest {

    @Test
    public void testSuccessfulLogin() {
        MemberLoginBean loginBean = new  MemberLoginBean();
        loginBean.setMemberData(new  MemberData());  // Inject real AdminData

        String username = "member";
        String password = "member123";
        loginBean.setUsername(username);
        loginBean.setPassword(password);

        String result = loginBean.login(username, password);

        assertEquals("memberDashboard", result);
        assertTrue(loginBean.isLoggedIn());
    }

    @Test
    public void testFailedLogin() {
        MemberLoginBean loginBean = new  MemberLoginBean();
        loginBean.setMemberData(new MemberData());

        String username = "notreal";
        String password = "wrongPass";
        loginBean.setUsername(username);
        loginBean.setPassword(password);

        String result = loginBean.login(username, password);

        assertNull(result);
        assertFalse(loginBean.isLoggedIn());
    }

    @Test
    public void testLogout() {
        MemberLoginBean loginBean = new MemberLoginBean();
        loginBean.setMemberData(new MemberData());

        String username = "member";
        String password = "member123";
        loginBean.setUsername(username);
        loginBean.setPassword(password);
        loginBean.login(username, password);
        
        String result = loginBean.logout();

        assertEquals("login", result);
        assertFalse(loginBean.isLoggedIn());
        assertNull(loginBean.getUsername());
        assertNull(loginBean.getPassword());
    }
}
