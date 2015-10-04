package springBootExample.model.repository;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import springBootExample.model.entity.User;
import springBootExample.model.json.UserDto;

/**
 * UserMapper transforms entity to dto objects and vice versa.
 */
@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto userToUserDto(User user);

    User userDtoToUser(UserDto userDto);
}