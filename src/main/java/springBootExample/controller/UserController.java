package springBootExample.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;
import springBootExample.model.entity.User;
import springBootExample.model.json.*;
import springBootExample.model.repository.UserMapper;
import springBootExample.model.repository.UserRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.Set;

/**
 *
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/user")
public class UserController {

    private UserRepository userRepository;

    private Validator validator = null;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    /**
     * @param id
     * @return JsonResponse
     */
    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    JsonResponse getUser(@PathVariable String id) {
        ResponseManager rm = new ResponseManager();

        if (!userRepository.userExists(id)) {
            rm.addError("general : User does not exist.");
            return rm.getResponse();
        }

        return UserMapper.INSTANCE.userToUserDto(userRepository.findOne(Long.parseLong(id)));
    }

    /**
     * @return Iterable
     */
    @RequestMapping(method = RequestMethod.GET)
    Iterable<UserDto> getUsers() {
        ArrayList<UserDto> result = new ArrayList<>();

        userRepository.findAll().forEach(
                entity -> result.add(UserMapper.INSTANCE.userToUserDto(entity))
        );

        return result;
    }

    /**
     * @param user
     * @return JsonResponse
     */
    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    JsonResponse addUser(@RequestBody UserDto user) {
        ResponseManager rm = new ResponseManager();

        Set<ConstraintViolation<UserDto>> constraintViolations = validator.validate(user);
        if (constraintViolations.size() > 0) {
            for (ConstraintViolation<UserDto> error : constraintViolations) {
                rm.addError(error.getPropertyPath().toString() + " : " + error.getMessage());
            }
            return rm.getResponse();
        }

        User entity = UserMapper.INSTANCE.userDtoToUser(user);

        try {
            userRepository.save(entity);
            return UserMapper.INSTANCE.userToUserDto(entity);
        } catch (Exception e) {
            rm.addError("general : Something went wrong, please contact admin.");
            log.error(e.toString());
        }
        return rm.getResponse();
    }

    /**
     * @param id
     * @param user
     * @return JsonResponse
     */
    @RequestMapping(method = RequestMethod.PUT, value = "{id}")
    public
    @ResponseBody
    JsonResponse updateUser(@PathVariable String id, @RequestBody UserDto user) {
        ResponseManager rm = new ResponseManager();

        if (!userRepository.userExists(id)) {
            rm.addError("general : User does not exist.");
            return rm.getResponse();
        }

        Set<ConstraintViolation<UserDto>> constraintViolations = validator.validate(user);
        if (constraintViolations.size() > 0) {
            for (ConstraintViolation<UserDto> error : constraintViolations) {
                rm.addError(error.getPropertyPath().toString() + " : " + error.getMessage());
            }
            return rm.getResponse();
        }

        User entity = UserMapper.INSTANCE.userDtoToUser(user);

        try {
            userRepository.save(entity);
            return UserMapper.INSTANCE.userToUserDto(entity);
        } catch (Exception e) {
            rm.addError("general : Something went wrong, please contact admin.");
            log.error(e.toString());
        }
        return rm.getResponse();
    }

    /**
     * Delete user
     *
     * @param id
     * @return JsonResponse
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "{id}")
    JsonResponse deleteUser(@PathVariable String id) {
        ResponseManager rm = new ResponseManager();

        if (!userRepository.userExists(id)) {
            rm.addError("general : User does not exist.");
            return rm.getResponse();
        }

        try {
            userRepository.delete(Long.parseLong(id));
        } catch (Exception e) {
            rm.addError("general : Something went wrong, please contact admin.");
            log.error(e.toString());
        }
        return rm.getResponse();
    }
}