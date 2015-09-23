package springBootExample.model.repository;

import org.apache.tomcat.jni.Local;
import org.springframework.data.repository.CrudRepository;
import springBootExample.model.entity.User;
import springBootExample.model.json.UserDto;

import java.time.LocalDate;
import java.util.Date;

public interface UserRepository extends CrudRepository<User, Long> {

    default UserDto getResponseFromUser(User entity) {
        return new UserDto(
                entity.getId(),
                entity.getEmail(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getDateOfBirth()
        );
    }

    default User createEntityFromResponse(UserDto user) {
        return new User(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getDateOfBirth()
        );
    }

    default void updateEntityFromResponse(UserDto user, User entity) {
        entity.setEmail(user.getEmail());
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setDateOfBirth(user.getDateOfBirth());
    }
}
