package work.emmanuel.training.ideas.iam_demo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AuthoritiesHolderDTO {
	private List<String>authorities;
}
