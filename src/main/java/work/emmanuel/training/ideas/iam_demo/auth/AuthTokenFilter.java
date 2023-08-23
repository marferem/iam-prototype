package work.emmanuel.training.ideas.iam_demo.auth;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.impl.DefaultClaims;
import work.emmanuel.training.ideas.iam_demo.redis.hash.Authorities;
import work.emmanuel.training.ideas.iam_demo.redis.repo.AuthoritiesRepository;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private AuthoritiesRepository authoritiesRepository;

	private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

	@SuppressWarnings("rawtypes")
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		logger.info("Running AuthTokenFilter.doFilterInternal");
		checkJwtExistenceAndValidate: {			
			try {
				logger.debug("Checking for jwt presence");
				String strJwt = parseJwt(request);
				if (strJwt == null) {
					logger.debug("Jwt not found");
					break checkJwtExistenceAndValidate;
				}
				logger.debug("Validating jwt");
				Jwt jwt = this.jwtUtils.parseToken(strJwt);
				logger.debug("Jwt is valid");
				
				if (logger.isTraceEnabled()) {
					logger.trace(" - jwt: {}", jwt);
				}
				
				DefaultClaims claims = (DefaultClaims) jwt.getBody();
				String jti = claims.getId();
				logger.debug(" - jti: {}", jti);
				logger.debug("Retrieving authorities from Redis");
				Optional<Authorities> optionalAuth = this.authoritiesRepository.findById(jti);
				if (!optionalAuth.isPresent()) {
					throw new IllegalStateException("Authorities not found for JWT");
				} 
				Authorities authorities = optionalAuth.get();
				Authentication authenticationToken = this.jwtUtils.parseAuthoritiesToAuthentication(authorities);
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			} catch (RuntimeException e) {
				throw e;
			}
		}

		filterChain.doFilter(request, response);
	}

	private String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");
		if (logger.isTraceEnabled()) logger.trace(" - headerAuth: " + headerAuth);
		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			String strJwt = headerAuth.substring(7);
			if (logger.isTraceEnabled()) logger.trace(" - strJwt: " + strJwt);
			return strJwt;
		}

		return null;
	}
}
