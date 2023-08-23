package work.emmanuel.training.ideas.iam_demo.auth;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import work.emmanuel.training.ideas.iam_demo.redis.hash.Authorities;
import work.emmanuel.training.ideas.iam_demo.redis.repo.AuthoritiesRepository;

@Component
@Data
public class JwtUtilsImpl implements JwtUtils {
	
	@Autowired
	private AuthoritiesRepository authoritiesRepository;
	
	@Value("${cbpa.jwt.secret}")
	private String jwtSecret;
	
	@Value("${cbpa.jwt.expiration_ms}")
	private int jwtExpirationMs;
	
	@Value("${cbpa.jwt.refresh_token_expiration_ms}")
	private long refreshTokenExpirationMs;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	private static final Logger logger = LoggerFactory.getLogger(JwtUtilsImpl.class);

	@Override
	public TokensDTO generateJwt(Authentication authentication) {
		logger.info("Generating jwt");
		if (logger.isTraceEnabled()) logger.trace(" - authentication: {}", authentication);
		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
		logger.debug("Creating jti");
		String jti = UUID.randomUUID().toString();
		logger.debug(" - jti: {}", jti);
		
		String refreshToken = UUID.randomUUID().toString();
		logger.debug(" - refreshToken: {}", refreshToken);
		
		Authorities authorities = Authorities.builder()
			.jti(jti)
			.refreshToken(refreshToken)
			.refreshTokenExpiration(this.calculateRefreshTokenExpiration())
			.values(userPrincipal.getAuthorities().stream().map(a -> a.getAuthority()).toList())
			.subject(userPrincipal.getUsername())
			.build();
		
		logger.debug("Persisting authorities in Redis");
		this.authoritiesRepository.save(authorities);
		
		logger.debug("Assembing JWT");		
		return this.generateJwt(authorities);
	}

	private Key key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}

	public String getUserNameFromJwtToken(String token) {
		JwtParser parser = Jwts.parserBuilder().setSigningKey(key()).build();
		return parser.parseClaimsJws(token).getBody().getSubject();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Jwt parseToken(String authToken) {
		logger.info("Validating token");
		if (logger.isTraceEnabled())
			logger.trace(" - authToken: " + authToken);
		
		try {
			JwtParser parser = Jwts.parserBuilder().setSigningKey(key()).build();
			Jwt jwt = parser.parse(authToken);
			return jwt;
		} catch (Exception e) {
			logger.info("Invalid token, e.message: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	@Override
	public TokensDTO refreshJwt(String refreshToken) {
		logger.info("Refreshing JWT");
		logger.debug(" - refreshToken: {}", refreshToken);
		Optional<Authorities>authoritiesOptional = this.authoritiesRepository.findOne(
			Example.of(Authorities.builder().refreshToken(refreshToken).build()));
		if (authoritiesOptional.isEmpty()) {
			logger.info("Invalid refresh token");
			throw new IllegalStateException("Invalid refresh token");
		}
		Authorities authorities = authoritiesOptional.get();
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(authorities.getSubject());		
		
		Authorities newAuthorities = Authorities.builder()
			.jti(UUID.randomUUID().toString())
			.refreshToken(UUID.randomUUID().toString())
			.subject(authorities.getSubject())
			.values(userDetails.getAuthorities().stream().map(a -> a.getAuthority()).toList())
			.refreshTokenExpiration(this.calculateRefreshTokenExpiration())
			.build();
		this.authoritiesRepository.save(newAuthorities);
		this.authoritiesRepository.delete(authorities);
		return this.generateJwt(newAuthorities);
	}

	@Override
	public Authentication parseAuthoritiesToAuthentication(Authorities authorities) {
		UserDetailsImpl userDetails = UserDetailsImpl.builder()
			.authorities(authorities.getValues().stream().map(v -> new SimpleGrantedAuthority(v)).toList())
			.username(authorities.getSubject())
			.build();
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
	    		userDetails, 
	    		null,
	    		userDetails.getAuthorities());
		return authenticationToken;
	}
	
	private TokensDTO generateJwt(Authorities authorities) {
		String jwt = Jwts
			.builder()
			.setId(authorities.getJti())
			.setSubject(authorities.getSubject())
			.setIssuedAt(new Date())
			.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
			.signWith(this.key(), SignatureAlgorithm.HS256)
			.compact();
		
		if (logger.isTraceEnabled()) {
			logger.trace(" - jwt: {}", jwt);
		}
			
		return TokensDTO.builder().accessToken(jwt).refreshToken(authorities.getRefreshToken()).build();
	}
	
	private Long calculateRefreshTokenExpiration() {
		return Instant.now().plusMillis(refreshTokenExpirationMs).toEpochMilli();
	}
}
