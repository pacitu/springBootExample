package springBootExample.controller;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import springBootExample.model.entity.User;
import springBootExample.model.json.UserDto;
import springBootExample.model.repository.UserRepository;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MockServletContext.class)
@WebAppConfiguration
public class UserControllerTest {

    private MockMvc mvc;

    @Mock
    private UserRepository mockRepo;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(new UserController(mockRepo)).build();
    }

    @Test
    public void getUser() throws Exception {
        Long userId = 1l;
        User user = new User("Pavel", "Kostadinov", "pacitu@abv.bg", "1985-04-04");
        user.setId(userId);
        when(mockRepo.userExists(userId.toString())).thenReturn(true);
        when(mockRepo.findOne(userId)).thenReturn(user);

        mvc.perform(MockMvcRequestBuilders.get("/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"id\":" + userId.toString() + ",\"email\":\"pacitu@abv.bg\",\"firstName\":\"Pavel\",\"lastName\":\"Kostadinov\",\"dateOfBirth\":\"1985-04-04\"}")));
    }

    @Test
    public void getMissingUser() throws Exception {
        Long userId = 1l;
        when(mockRepo.userExists(userId.toString())).thenReturn(false);

        mvc.perform(MockMvcRequestBuilders.get("/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"errors\":[\"general : User does not exist.\"]}")));
    }

    @Test
    public void getUsers() throws Exception {
        List<User> expectedPeople = asList(new User("Pavel", "Kostadinov", "pacitu@abv.bg", "1985-04-04"));
        when(mockRepo.findAll()).thenReturn(expectedPeople);

        mvc.perform(MockMvcRequestBuilders.get("/user"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("[{\"id\":0,\"email\":\"pacitu@abv.bg\",\"firstName\":\"Pavel\",\"lastName\":\"Kostadinov\",\"dateOfBirth\":\"1985-04-04\"}]")));
    }

    @Test
    public void deleteUser() throws Exception {
        Long userId = 1l;
        when(mockRepo.userExists(userId.toString())).thenReturn(true);


        mvc.perform(MockMvcRequestBuilders.delete("/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"success\":true}")));

        Mockito.verify(mockRepo, times(1)).delete(userId);
    }

    @Test
    public void deleteMissingUser() throws Exception {
        Long userId = 1l;
        when(mockRepo.userExists(userId.toString())).thenReturn(false);

        mvc.perform(MockMvcRequestBuilders.delete("/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"errors\":[\"general : User does not exist.\"]}")));
    }

    @Test
    public void updateUser() throws Exception {
        Long userId = 1l;
        UserDto userDto = new UserDto(userId, "pacitu@abv.bg", "Pavel", "Kostadinov", "1985-04-04");
        Gson gson = new Gson();
        String json = gson.toJson(userDto);

        when(mockRepo.userExists(userId.toString())).thenReturn(true);

        User user = new User("Pavel", "Kostadinov", "pacitu@abv.bg", "1985-04-04");
        user.setId(userId);

        mvc.perform(MockMvcRequestBuilders.put("/user/" + userId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"id\":" + userId.toString() + ",\"email\":\"pacitu@abv.bg\",\"firstName\":\"Pavel\",\"lastName\":\"Kostadinov\",\"dateOfBirth\":\"1985-04-04\"}")));

        Mockito.verify(mockRepo, times(1)).save(user);
    }

    @Test
    public void updateUserMalformedEmail() throws Exception {
        Long userId = 1l;
        UserDto userDto = new UserDto(userId, "Hello world!", "Pavel", "Kostadinov", "1985-04-04");
        Gson gson = new Gson();
        String json = gson.toJson(userDto);

        when(mockRepo.userExists(userId.toString())).thenReturn(true);

        User user = new User("Pavel", "Kostadinov", "pacitu@abv.bg", "1985-04-04");
        user.setId(userId);

        mvc.perform(MockMvcRequestBuilders.put("/user/" + userId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"errors\":[\"email : not a well-formed email address\"]}")));

        Mockito.verify(mockRepo, times(0)).save(user);
    }

    @Test
    public void updateUserMissingEmail() throws Exception {
        Long userId = 1l;
        UserDto userDto = new UserDto(userId, null, "Pavel", "Kostadinov", "1985-04-04");
        Gson gson = new Gson();
        String json = gson.toJson(userDto);

        when(mockRepo.userExists(userId.toString())).thenReturn(true);

        User user = new User("Pavel", "Kostadinov", "pacitu@abv.bg", "1985-04-04");
        user.setId(userId);

        mvc.perform(MockMvcRequestBuilders.put("/user/" + userId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"errors\":[\"email : may not be null\"]}")));

        Mockito.verify(mockRepo, times(0)).save(user);
    }

    @Test
    public void updateUserMissingFirstName() throws Exception {
        Long userId = 1l;
        UserDto userDto = new UserDto(userId, "pacitu@abv.bg", null, "Kostadinov", "1985-04-04");
        Gson gson = new Gson();
        String json = gson.toJson(userDto);

        when(mockRepo.userExists(userId.toString())).thenReturn(true);

        User user = new User("Pavel", "Kostadinov", "pacitu@abv.bg", "1985-04-04");
        user.setId(userId);

        mvc.perform(MockMvcRequestBuilders.put("/user/" + userId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"errors\":[\"firstName : may not be null\"]}")));

        Mockito.verify(mockRepo, times(0)).save(user);
    }

    @Test
    public void updateUserMissingLastName() throws Exception {
        Long userId = 1l;
        UserDto userDto = new UserDto(userId, "pacitu@abv.bg", "Pavel", null, "1985-04-04");
        Gson gson = new Gson();
        String json = gson.toJson(userDto);

        when(mockRepo.userExists(userId.toString())).thenReturn(true);

        User user = new User("Pavel", "Kostadinov", "pacitu@abv.bg", "1985-04-04");
        user.setId(userId);

        mvc.perform(MockMvcRequestBuilders.put("/user/" + userId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"errors\":[\"lastName : may not be null\"]}")));

        Mockito.verify(mockRepo, times(0)).save(user);
    }

    @Test
    public void updateUserMissingDateOfBirth() throws Exception {
        Long userId = 1l;
        UserDto userDto = new UserDto(userId, "pacitu@abv.bg", "Pavel", "Kostadinov", null);
        Gson gson = new Gson();
        String json = gson.toJson(userDto);

        when(mockRepo.userExists(userId.toString())).thenReturn(true);

        User user = new User("Pavel", "Kostadinov", "pacitu@abv.bg", "1985-04-04");
        user.setId(userId);

        mvc.perform(MockMvcRequestBuilders.put("/user/" + userId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"errors\":[\"dateOfBirth : may not be null\"]}")));

        Mockito.verify(mockRepo, times(0)).save(user);
    }

    @Test
    public void addUser() throws Exception {
        Long userId = 1l;
        UserDto userDto = new UserDto(userId, "pacitu@abv.bg", "Pavel", "Kostadinov", "1985-04-04");
        Gson gson = new Gson();
        String json = gson.toJson(userDto);

        User user = new User("Pavel", "Kostadinov", "pacitu@abv.bg", "1985-04-04");
        user.setId(userId);

        mvc.perform(MockMvcRequestBuilders.post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"id\":" + userId.toString() + ",\"email\":\"pacitu@abv.bg\",\"firstName\":\"Pavel\",\"lastName\":\"Kostadinov\",\"dateOfBirth\":\"1985-04-04\"}")));

        Mockito.verify(mockRepo, times(1)).save(user);
    }

    @Test
    public void addUserMalformedEmail() throws Exception {
        Long userId = 1l;
        UserDto userDto = new UserDto(userId, "Hello world!", "Pavel", "Kostadinov", "1985-04-04");
        Gson gson = new Gson();
        String json = gson.toJson(userDto);

        User user = new User("Pavel", "Kostadinov", "pacitu@abv.bg", "1985-04-04");
        user.setId(userId);

        mvc.perform(MockMvcRequestBuilders.post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"errors\":[\"email : not a well-formed email address\"]}")));

        Mockito.verify(mockRepo, times(0)).save(user);
    }

    @Test
    public void addUserMissingEmail() throws Exception {
        Long userId = 1l;
        UserDto userDto = new UserDto(userId, null, "Pavel", "Kostadinov", "1985-04-04");
        Gson gson = new Gson();
        String json = gson.toJson(userDto);

        User user = new User("Pavel", "Kostadinov", "pacitu@abv.bg", "1985-04-04");
        user.setId(userId);

        mvc.perform(MockMvcRequestBuilders.post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"errors\":[\"email : may not be null\"]}")));

        Mockito.verify(mockRepo, times(0)).save(user);
    }

    @Test
    public void addUserMissingFirstName() throws Exception {
        Long userId = 1l;
        UserDto userDto = new UserDto(userId, "pacitu@abv.bg", null, "Kostadinov", "1985-04-04");
        Gson gson = new Gson();
        String json = gson.toJson(userDto);

        User user = new User("Pavel", "Kostadinov", "pacitu@abv.bg", "1985-04-04");
        user.setId(userId);

        mvc.perform(MockMvcRequestBuilders.post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"errors\":[\"firstName : may not be null\"]}")));

        Mockito.verify(mockRepo, times(0)).save(user);
    }

    @Test
    public void addUserMissingLastName() throws Exception {
        Long userId = 1l;
        UserDto userDto = new UserDto(userId, "pacitu@abv.bg", "Pavel", null, "1985-04-04");
        Gson gson = new Gson();
        String json = gson.toJson(userDto);

        User user = new User("Pavel", "Kostadinov", "pacitu@abv.bg", "1985-04-04");
        user.setId(userId);

        mvc.perform(MockMvcRequestBuilders.post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"errors\":[\"lastName : may not be null\"]}")));

        Mockito.verify(mockRepo, times(0)).save(user);
    }

    @Test
    public void addUserMissingDateOfBirth() throws Exception {
        Long userId = 1l;
        UserDto userDto = new UserDto(userId, "pacitu@abv.bg", "Pavel", "Kostadinov", null);
        Gson gson = new Gson();
        String json = gson.toJson(userDto);

        User user = new User("Pavel", "Kostadinov", "pacitu@abv.bg", "1985-04-04");
        user.setId(userId);

        mvc.perform(MockMvcRequestBuilders.post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"errors\":[\"dateOfBirth : may not be null\"]}")));

        Mockito.verify(mockRepo, times(0)).save(user);
    }
}