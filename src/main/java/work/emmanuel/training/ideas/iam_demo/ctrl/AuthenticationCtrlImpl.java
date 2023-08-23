package work.emmanuel.training.ideas.iam_demo.ctrl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import work.emmanuel.training.ideas.iam_demo.auth.JwtUtils;
import work.emmanuel.training.ideas.iam_demo.auth.TokensDTO;
import work.emmanuel.training.ideas.iam_demo.dto.LoginRequestDTO;
import work.emmanuel.training.ideas.iam_demo.dto.RefreshTokenRequestDTO;

@RestController
@RequestMapping("/auth")
public class AuthenticationCtrlImpl implements AuthenticationCtrl {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationCtrlImpl.class);
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	JwtUtils jwtUtils;

	@Override
	@PostMapping("/login")
	public TokensDTO login(
			@RequestBody LoginRequestDTO loginRequest) {
		logger.info("Running /login");
		logger.info("Authenticating");
		Authentication authentication = 
			this.authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
					loginRequest.getUser(),  
					loginRequest.getPassword()));
		return jwtUtils.generateJwt(authentication);
	}

	@Override
	@PostMapping("/refresh-token")
	public TokensDTO refreshToken(
			@RequestBody RefreshTokenRequestDTO refreshTokenRequest) {
		logger.info("Refreshing token");
		if (logger.isTraceEnabled()) {
			logger.trace(" - refresh token: {}", refreshTokenRequest.getRefreshToken());
		}
		return this.jwtUtils.refreshJwt(refreshTokenRequest.getRefreshToken());
	}
}
