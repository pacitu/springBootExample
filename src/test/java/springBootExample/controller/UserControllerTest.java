package springBootExample.controller;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import springBootExample.model.entity.User;
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
    public void getUsers() throws Exception {
        List<User> expectedPeople = asList(new User("Pavel", "Kostadinov", "pacitu@abv.bg", "1985-04-04"));
        when(mockRepo.findAll()).thenReturn(expectedPeople);

        mvc.perform(MockMvcRequestBuilders.get("/user"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("[{\"id\":0,\"email\":\"pacitu@abv.bg\",\"firstName\":\"Pavel\",\"lastName\":\"Kostadinov\",\"dateOfBirth\":\"1985-04-04\"}]")));
    }
}