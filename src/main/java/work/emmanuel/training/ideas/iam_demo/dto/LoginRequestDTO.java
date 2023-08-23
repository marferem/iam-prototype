package work.emmanuel.training.ideas.iam_demo.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
	private String user;
	private String password;
}
