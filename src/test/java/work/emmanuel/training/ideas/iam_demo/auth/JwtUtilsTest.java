package work.emmanuel.training.ideas.iam_demo.auth;

import org.junit.jupiter.api.BeforeAll;

public class JwtUtilsTest {
	private static JwtUtils jwtUtils;
	
	@BeforeAll
	static void setup () {
		JwtUtilsImpl impl = new JwtUtilsImpl();
		impl.setAuthoritiesRepository(null);
	}
}
