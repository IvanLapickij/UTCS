package utcs;

import MemberRegistration.RegistrationBean;
import Members.Member;
import Members.MemberData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegistrationBeanTest {

    private RegistrationBean registrationBean;
    private MemberData memberData;

    @BeforeEach
    public void setup() {
        registrationBean = new RegistrationBean();
        memberData = new MemberData(); // Contains default members
        registrationBean.setMemberData(memberData);
    }

    @Test
    public void testSuccessfulRegistration() {
        registrationBean.setUsername("john.doe");
        registrationBean.setFirstName("John");
        registrationBean.setLastName("Doe");
        registrationBean.setEmailAddress("newuser1@example.com");
        registrationBean.setPassword("strongpass");
        registrationBean.setConfirmPassword("strongpass");

        String result = registrationBean.register();

        assertEquals("show-registration", result);

        Member added = memberData.doesUsernameExist("john.doe");
        assertNotNull(added);
        assertEquals("newuser1@example.com", added.getEmailAddress());
    }

    @Test
    public void testDuplicateUsername() {
        registrationBean.setUsername("member"); // already exists in MemberData
        registrationBean.setEmailAddress("unique@example.com");
        registrationBean.setPassword("validpass");
        registrationBean.setConfirmPassword("validpass");

        String result = registrationBean.register();

        assertNull(result);
    }

    @Test
    public void testDuplicateEmail() {
        registrationBean.setUsername("uniqueUser");
        registrationBean.setEmailAddress("member@member.ie"); // already in MemberData
        registrationBean.setPassword("validpass");
        registrationBean.setConfirmPassword("validpass");

        String result = registrationBean.register();

        assertNull(result);
    }

    @Test
    public void testPasswordMismatch() {
        registrationBean.setUsername("user123");
        registrationBean.setEmailAddress("user123@example.com");
        registrationBean.setPassword("password1");
        registrationBean.setConfirmPassword("password2");

        String result = registrationBean.register();

        assertNull(result);
    }

    @Test
    public void testPasswordTooShort() {
        registrationBean.setUsername("shortpass");
        registrationBean.setEmailAddress("short@example.com");
        registrationBean.setPassword("123");
        registrationBean.setConfirmPassword("123");

        String result = registrationBean.register();

        assertNull(result);
    }

    @Test
    public void testCancel() {
        assertEquals("login", registrationBean.cancel());
    }

    @Test
    public void testGetObscuredPassword() {
        registrationBean.setPassword("mypassword");
        assertEquals("m...d", registrationBean.getObscuredPassword());
    }
}
