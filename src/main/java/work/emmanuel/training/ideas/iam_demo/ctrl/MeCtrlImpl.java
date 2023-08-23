package work.emmanuel.training.ideas.iam_demo.ctrl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import work.emmanuel.training.ideas.iam_demo.dto.AuthoritiesHolderDTO;

@RestController
@RequestMapping("/me")
public class MeCtrlImpl implements MeCtrl {

	private static final Logger logger = LoggerFactory.getLogger(MeCtrlImpl.class);
	
	@Override
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/authorities")
	public AuthoritiesHolderDTO getAuthorities() {
		logger.info("Getting current user authorities");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (logger.isTraceEnabled()) {
			logger.trace(" - authentication: {}", authentication);
		}
		List<String> authorities = ((UsernamePasswordAuthenticationToken) authentication)
			.getAuthorities()
			.stream()
			.map(a -> a.getAuthority())
			.toList();		
		if (logger.isTraceEnabled()) {
			logger.trace(" - authorities: {}", authorities);
		}

		return AuthoritiesHolderDTO.builder().authorities(authorities).build();
	}

}
