package net.tysonsorensen.userAccount.web;

import net.tysonsorensen.userAccount.services.UserService;
import net.tysonsorensen.userAccount.web.controllers.UserController;
import net.tysonsorensen.userAccount.web.helpers.UserForm;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testLogin() throws Exception {
        mvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(model().attributeDoesNotExist("message"));
    }

    @Test
    public void testLogin_errors() throws Exception {
        mvc.perform(get("/login")
        .param("error", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("error", "Your username and password is invalid."))
                .andExpect(model().attributeDoesNotExist("message"));
    }

    @Test
    public void testLogin_logout() throws Exception {
        mvc.perform(get("/login")
                .param("logout", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("message", "You have been logged out successfully."))
                .andExpect(model().attributeDoesNotExist("error"));
    }

    @Test
    public void testRegistration_get() throws Exception {
        mvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(model().attribute("userForm", new UserForm()));
    }

    @Test
    public void testRegistration_post() throws Exception {
        mvc.perform(post("/registration")
                .param("userName", "test")
                .param("firstName", "test")
                .param("lastName", "test")
                .param("password", "test1234")
                .param("email", "test@test.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void testRegistration_post_emptyName() throws Exception {
        mvc.perform(post("/registration")
                .param("userName", "")
                .param("firstName", "test")
                .param("lastName", "test")
                .param("password", "test1234")
                .param("email", "test@test.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"));
    }

    @Test
    public void testRegistration_post_emptyFirstName() throws Exception {
        mvc.perform(post("/registration")
                .param("userName", "test")
                .param("firstName", "")
                .param("lastName", "test")
                .param("password", "test1234")
                .param("email", "test@test.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"));
    }

    @Test
    public void testRegistration_post_emptyLastName() throws Exception {
        mvc.perform(post("/registration")
                .param("userName", "test")
                .param("firstName", "test")
                .param("lastName", "")
                .param("password", "test1234")
                .param("email", "test@test.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"));
    }

    @Test
    public void testRegistration_post_emptyEmail() throws Exception {
        mvc.perform(post("/registration")
                .param("userName", "test")
                .param("firstName", "test")
                .param("lastName", "test")
                .param("password", "test1234")
                .param("email", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"));
    }

    @Test
    public void testRegistration_post_invalidEmail() throws Exception {
        mvc.perform(post("/registration")
                .param("userName", "test")
                .param("firstName", "test")
                .param("lastName", "test")
                .param("password", "test1234")
                .param("email", "testest"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"));
    }

    @Test
    public void testRegistration_post_emptyPassword() throws Exception {
        mvc.perform(post("/registration")
                .param("userName", "test")
                .param("firstName", "test")
                .param("lastName", "test")
                .param("password", "")
                .param("email", "test@test.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"));
    }

    @Test
    public void testRegistration_post_tooShortPassword() throws Exception {
        mvc.perform(post("/registration")
                .param("userName", "test")
                .param("firstName", "test")
                .param("lastName", "test")
                .param("password", "test123")
                .param("email", "test@test.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"));
    }

}