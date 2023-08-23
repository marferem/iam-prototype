package work.emmanuel.training.ideas.iam_demo.redis.hash;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@RedisHash(
		value = "RefreshToken", 
		timeToLive = 60 * 60 * 24 // ONE DAY
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {
	@Id
	private String token;
	private Long expirationTime;
	private Authorities authorities;
}
