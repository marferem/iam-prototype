package work.emmanuel.training.ideas.iam_demo;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;

import work.emmanuel.training.ideas.iam_demo.redis.hash.Authorities;
import work.emmanuel.training.ideas.iam_demo.redis.hash.RefreshToken;
import work.emmanuel.training.ideas.iam_demo.redis.repo.AuthoritiesRepository;
import work.emmanuel.training.ideas.iam_demo.redis.repo.RefreshTokenRepository;

@SpringBootTest()
@TestMethodOrder(OrderAnnotation.class)
public class AuhtoritiesRepositoryTest {

	private static final Logger logger = LoggerFactory.getLogger(AuhtoritiesRepositoryTest.class);
	
	@Autowired
	private AuthoritiesRepository authoritiesRepository;	
	
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	
	private static int order = 0;
	
	private static String jti;
	private static String refreshToken;

	@BeforeAll
	static void setup() {
		jti = UUID.randomUUID().toString();
		refreshToken = UUID.randomUUID().toString();
	}
	
	@Test
	@Order(0)
	void testClear() {
		logger.info("clear order: {}", ++order);
//		this.authoritiesRepository.deleteAll();
//		this.refreshTokenRepository.deleteAll();
	}
	
	@Test
	@Order(1)
	void testSaveAuthorities() {

		logger.info("testSaveAuthorities order: {}", ++order);
		Authorities authorities = Authorities.builder()
			.jti(jti)
			.values(List.of("ADMIN", "CURTAILMENT_ADMIN"))
			.refreshToken(refreshToken)
			.build();
		this.authoritiesRepository.save(authorities);
	}
	
	@Test
	@Order(2)
	void testCount() {
		logger.info("testCount order: {}", ++order);
		long count = this.authoritiesRepository.count();
		logger.info(" - count: {}", count);
	}
		
	@Test
	@Order(3)
	void testGetById() {
		logger.info("testGetById: {}", ++order);
		Authorities byId = this.authoritiesRepository.findById(jti).get();
		logger.info(" - byId: {}", byId);
		
		Authorities byRefreshToken = this.authoritiesRepository.findOne(
				Example.of(Authorities.builder().refreshToken(refreshToken).build()))
				.get();
		logger.info(" - byRefreshToken: {}", byRefreshToken);

	}
	
}
