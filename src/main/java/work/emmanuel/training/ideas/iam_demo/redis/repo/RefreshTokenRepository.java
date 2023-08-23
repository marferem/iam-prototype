package work.emmanuel.training.ideas.iam_demo.redis.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import work.emmanuel.training.ideas.iam_demo.redis.hash.RefreshToken;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

}
