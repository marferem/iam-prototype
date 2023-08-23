package work.emmanuel.training.ideas.iam_demo.auth;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
@Qualifier("jwtAuthorizationFilter")
public class JWTAuthorizationFilter extends OncePerRequestFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(JWTAuthorizationFilter.class);

	private final String HEADER = "Authorization";
	private final String PREFIX = "Bearer ";
	@Autowired
	private JwtUtils jwtUtils;

	@Override
	protected void doFilterInternal(
			HttpServletRequest request, 
			HttpServletResponse response, 
			FilterChain chain) throws ServletException, IOException {
		logger.info("Running JWTAuthorizationFilter.doFilterInternal");
		try {
			if (existsJWTToken(request)) {
//				Claims claims = validateToken(request);
//				if (claims.get("authorities") != null) {
//					setUpSpringAuthentication(claims);
//				} else {
//					SecurityContextHolder.clearContext();
//				}
			} else {
					SecurityContextHolder.clearContext();
			}
			chain.doFilter(request, response);
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
			return;
		}
	}	

//	private boolean validateToken(HttpServletRequest request) {
//		String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
//		return this.jwtUtils.parseToken(jwtToken);
//	}

	/**
	 * Metodo para autenticarnos dentro del flujo de Spring
	 * 
	 * @param claims
	 */
	private void setUpSpringAuthentication(Claims claims) {
		@SuppressWarnings("unchecked")
		List<String> authorities = (List) claims.get("authorities");

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			claims.getSubject(), 
			null,
			authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
		SecurityContextHolder.getContext().setAuthentication(auth);

	}

	private boolean existsJWTToken(HttpServletRequest request) {
		String authenticationHeader = request.getHeader(HEADER);
		if (authenticationHeader == null || !authenticationHeader.startsWith(PREFIX))
			return false;
		return true;
	}

}