package work.emmanuel.training.ideas.iam_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import work.emmanuel.training.ideas.iam_demo.redis.hash.Authorities;

@SpringBootApplication
@EnableRedisRepositories
public class IamDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(IamDemoApplication.class, args);
	}

	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
	    return new JedisConnectionFactory();
	}

	@Bean
	public RedisTemplate<String, Authorities> redisTemplate() {
	    RedisTemplate<String, Authorities> template = new RedisTemplate<>();
	    template.setConnectionFactory(jedisConnectionFactory());
	    
	    return template;
	}
}
