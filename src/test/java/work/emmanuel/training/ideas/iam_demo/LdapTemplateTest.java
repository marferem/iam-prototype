package work.emmanuel.training.ideas.iam_demo;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;

@SpringBootTest
public class LdapTemplateTest {
	
	@Autowired
	private LdapTemplate ldapTemplate;
	
	@Test
	void testBind() {
		DirContextAdapter marferem = (DirContextAdapter)ldapTemplate.lookup("cn=marferem,ou=users,dc=example,dc=org");
		
		System.out.println("marferem: " + marferem);
		
		boolean success = ldapTemplate.authenticate(
				"ou=users,dc=example,dc=org", 
				"cn=marferem", 
				"holamundo");
		assertTrue(success);
		
		boolean fail = ldapTemplate.authenticate(
				"ou=users,dc=example,dc=org", 
				"cn=marferem", 
				"mundohola");
		assertFalse(fail);
	}
}
