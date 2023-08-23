package work.emmanuel.training.ideas.iam_demo.auth;

import org.springframework.security.core.Authentication;
import io.jsonwebtoken.Jwt;
import work.emmanuel.training.ideas.iam_demo.redis.hash.Authorities;

public interface JwtUtils {
	TokensDTO refreshJwt(String refreshToken);
	TokensDTO generateJwt(Authentication authentication);
	String getUserNameFromJwtToken(String token);
	@SuppressWarnings("rawtypes")
	Jwt parseToken(String authToken);
	Authentication parseAuthoritiesToAuthentication(Authorities authorities);	
}
