package springBootExample.model.repository;

import org.springframework.data.repository.CrudRepository;
import springBootExample.model.entity.User;

/**
 * UserRepository
 */
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     *
     * @param id
     * @return if user with such id exists.
     */
    default boolean userExists(String id) {
        return this.findOne(Long.parseLong(id)) != null;
    }



}
