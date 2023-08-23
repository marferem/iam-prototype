package work.emmanuel.training.ideas.iam_demo.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@Qualifier("customAuthenticationProvider")
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Value("${cbpa.ldap.users_base_dn}")
	private String usersBaseDN;
	@Value("${cbpa.ldap.users_dnprefix}")
	private String usersDNPrefix;
	
	@Autowired
	private LdapTemplate ldapTemplate;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        
        boolean authenticates = ldapTemplate.authenticate(
				this.usersBaseDN, 
				this.usersDNPrefix + name, 
				password);
        
        if (!authenticates) {
        	AuthenticationException exception = new BadCredentialsException("invalid credentials");
        	throw exception;
        }
        
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(name);
        
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        		userDetails, 
        		password,
        		userDetails.getAuthorities());
		return authenticationToken;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
