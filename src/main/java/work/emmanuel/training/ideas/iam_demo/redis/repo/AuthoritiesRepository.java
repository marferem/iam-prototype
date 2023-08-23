package work.emmanuel.training.ideas.iam_demo.redis.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import work.emmanuel.training.ideas.iam_demo.redis.hash.Authorities;

@Repository
public interface AuthoritiesRepository extends 
		CrudRepository<Authorities, String>, 
		QueryByExampleExecutor<Authorities> {
	
}
