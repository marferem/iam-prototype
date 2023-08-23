package work.emmanuel.training.ideas.iam_demo.ctrl;

import work.emmanuel.training.ideas.iam_demo.auth.TokensDTO;
import work.emmanuel.training.ideas.iam_demo.dto.LoginRequestDTO;
import work.emmanuel.training.ideas.iam_demo.dto.RefreshTokenRequestDTO;

public interface AuthenticationCtrl {
	
	TokensDTO login(LoginRequestDTO loginRequest);
	TokensDTO refreshToken(RefreshTokenRequestDTO refreshTokenRequest);
}
