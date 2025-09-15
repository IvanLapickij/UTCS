package Members;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MembersTest {
    private Member member;
   
    @BeforeEach
    void setUp() {
    	member = new Member(null, null, null, null, null);
    }
   
    @Test
    void testFindAdminSuccess() {
    	MemberData memberData = new MemberData();

    	Member result = memberData.findMember("member", "member123");
        assertNotNull(result);
        assertEquals("member", result.getUsername());
        assertEquals("member123", result.getPassword());
    }

    @Test
    void testFindAdminFailure() {
    	MemberData memberData = new MemberData();

    	Member result = memberData.findMember("fakeUser", "fakePass");
        assertNull(result);
    }
    
    @Test
    void  testMemberGettersSetters() {
    	String username = "test",  firstName = "firstTest", lastName = "lastTest", emailAddress = "test@test.ie", password = "test123";
    	
    	Member member = new Member(null, null, null, null, null);
    	
    	member.setUsername(username);
    	member.setFirstName(firstName);
    	member.setLastName(lastName);
    	member.setPassword(password);
    	member.setEmailAddress(emailAddress);
    	
    	assertEquals(username, member.getUsername());
    	assertEquals(firstName, member.getFirstName());
    	assertEquals(lastName, member.getLastName());
    	assertEquals(emailAddress, member.getEmailAddress());
    	assertEquals(password, member.getPassword());
    	
    }
    
    @Test 
    void testMemberToString() {
    	String username = "test",  firstName = "firstTest", lastName = "lastTest", emailAddress = "test@test.ie", password = "test123";
    	Member newMember = new Member(username, firstName, lastName, emailAddress, password);
    	
    	String expected =  "Member:[" + username + ", " + firstName + ", " + lastName + ", " + emailAddress + "]";
    	
    	assertEquals(expected, newMember.toString());
    }
   

}