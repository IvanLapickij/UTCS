package Members;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MembersDataTest {

	@Test
	 public void testfindDefaultmember() {
		// Default member
		MemberData data = new MemberData();
		Member UTCS_User = data.findMember("member", "member123");
		
		assertNotNull(UTCS_User);
		assertEquals("member", UTCS_User.getUsername());
	
	}
	
	@Test
	 public void testfindothermember() {
		//Testing other members
		MemberData data = new MemberData();
		Member UTCS_User = data.findMember("testMember", "test123");
		
		assertNotNull(UTCS_User);
		assertEquals("testMember", UTCS_User.getUsername());
	
	}

	@Test
	 public void testfaillfindmemberwrongPassword() {
		// wrong password 
		MemberData data = new MemberData();
		Member UTCS_User = data.findMember("member", "wrongPassword");
		
		assertNull(UTCS_User);
		
	}

	@Test
	 public void testfailfindmemberwronguser() {
		// User name is not exist
		MemberData data = new MemberData();
		Member UTCS_User = data.findMember("notExist", "123456789");
		
		assertNull(UTCS_User);
	}

	
	@Test
	 public void testdoseUsernameExist() {
		// check if user name exist
		MemberData data = new MemberData();
		Member UTCS_User = data.doesUsernameExist("testMember");
		
		assertNotNull(UTCS_User);
		assertEquals("testMember", UTCS_User.getUsername());
	}

	@Test
	 public void testUsernameNOTExist() {
		// check if user name exist
		MemberData data = new MemberData();
		Member UTCS_User = data.doesUsernameExist("CSE");
		
		assertNull(UTCS_User);
	}
	
	@Test
	 public void testMemberGettersSetters() {
		// Default member
		MemberData data = new MemberData();
		Member testMember = new Member(null, null,null, null, null);
		String username = "test", firstname = "test", lastname = "test", email = "test@test.com", paswword = "test123";
		
		testMember.setUsername(username);
		testMember.setFirstName(firstname);
		testMember.setLastName(lastname);
		testMember.setEmailAddress(email);
		testMember.setPassword(paswword);
		
		data.addMember(testMember);
		Member UTCS_User = data.findMember("test", "test123");
		
		assertNotNull(UTCS_User);
		assertEquals("test", UTCS_User.getUsername());
		assertEquals("test", UTCS_User.getFirstName());
		assertEquals("test", UTCS_User.getLastName());
		assertEquals("test@test.com", UTCS_User.getEmailAddress());
		assertEquals("test123", UTCS_User.getPassword());
	
	}
	
}
