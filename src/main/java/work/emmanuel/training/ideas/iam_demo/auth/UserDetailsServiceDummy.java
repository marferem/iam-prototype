package work.emmanuel.training.ideas.iam_demo.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceDummy implements UserDetailsService {

	private static final Map<String, UserDetailsImpl> DUMMY_DATA;
	static {
		UserDetailsImpl marferem = new UserDetailsImpl();
		marferem.setUsername("marferem");
		marferem.setAuthorities(List.of(
			new SimpleGrantedAuthority("ROLE_ONE"),
			new SimpleGrantedAuthority("ROLE_TWO"),
			new SimpleGrantedAuthority("ROLE_THREE"),
			new SimpleGrantedAuthority("EDIT_WORDING"),
			new SimpleGrantedAuthority("ADMIN_CURTAILMENT")
		));
		DUMMY_DATA = new HashMap<String, UserDetailsImpl>();
		DUMMY_DATA.put("marferem", marferem);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails userDetails = DUMMY_DATA.get(username);
		if (userDetails == null) 
			throw new UsernameNotFoundException("Username not found with username: " + username);
		return userDetails;
	}
	
}
