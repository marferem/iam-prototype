package work.emmanuel.training.ideas.iam_demo.ctrl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestCtrl {
	
	private static final Logger logger = LoggerFactory.getLogger(TestCtrl.class);
	
	@GetMapping("/the-one")
	@PreAuthorize("hasRole('ONE')")
	public String testTheOne() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		logger.debug(" - currentPrincipalName: {}", currentPrincipalName);
		return "The one";
	}
}
