package work.emmanuel.training.ideas.iam_demo.redis.hash;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(
	value = "Authorities", 
	timeToLive = 60 * 60 * 24// ONE DAY
)
public class Authorities implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private String jti;
	private String refreshToken;
	private Long refreshTokenExpiration;
	private List<String>values;
	private String subject;
}
