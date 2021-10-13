package Rest.Repositories;

import Rest.Entities.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@Repository
public interface IUserCollectionRepository extends CrudRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
