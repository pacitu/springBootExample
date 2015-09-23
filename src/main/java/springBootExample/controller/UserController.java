package springBootExample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.*;
import springBootExample.model.entity.User;
import springBootExample.model.json.*;
import springBootExample.model.repository.UserRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Iterator;
import java.util.Set;

/**
 *
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/user")
public class UserController {

    /**
     *
     */
    @Autowired
    private UserRepository userRepository;

    private Validator validator = null;

    public UserController() {
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
        User entity = userRepository.findOne(Long.parseLong(id));

        if (entity == null) {
            rm.addError("User does not exist.");
            return rm.getResponse();
        }

        return userRepository.getResponseFromUser(entity);
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

        if( constraintViolations.size() > 0 ) {
            Iterator<ConstraintViolation<UserDto>> iterator = constraintViolations.iterator();
            while(iterator.hasNext()) {
                ConstraintViolation<UserDto> error = iterator.next();
                rm.addError(error.getPropertyPath().toString() +  " : " + error.getMessage());
            }
            return rm.getResponse();
        }

        User entity = userRepository.createEntityFromResponse(user);
        userRepository.save(entity);
        return userRepository.getResponseFromUser(entity);
    }

    /**
     * @param id
     * @param user
     * @return JsonResponse
     */
    @RequestMapping(method = RequestMethod.PUT, value = "{id}")
    public
    @ResponseBody
    JsonResponse update(@PathVariable String id, @RequestBody UserDto user) {
        ResponseManager rm = new ResponseManager();
        User entity = userRepository.findOne(Long.parseLong(id));

        if (entity == null) {
            rm.addError("User does not exist.");
            return rm.getResponse();
        }
        //TODO add validation.
        userRepository.updateEntityFromResponse(user, entity);
        userRepository.save(entity);
        return userRepository.getResponseFromUser(entity);
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

        //TODO replace try catch with validation
        try {
            userRepository.delete(Long.parseLong(id));
        } catch (EmptyResultDataAccessException e) {
            rm.addError("User does not exist.");
        } catch (Exception e) {
            rm.addError("Something went wrong, please contact admin.");
        }
        return rm.getResponse();
    }
    //TODO add exception logging.
    //TODO figure out error messaging pattern or s.th
}