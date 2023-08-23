package work.emmanuel.training.ideas.iam_demo.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokensDTO {
	private String accessToken;
	private String refreshToken;
}
