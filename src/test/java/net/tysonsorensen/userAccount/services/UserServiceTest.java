package net.tysonsorensen.userAccount.services;

import net.tysonsorensen.userAccount.data.entities.UserEntity;
import net.tysonsorensen.userAccount.data.repositories.RoleRepository;
import net.tysonsorensen.userAccount.data.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    private UserRepository userRepository = mock(UserRepository.class);
    private RoleRepository roleRepository = mock(RoleRepository.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private UserService uut;

    @Before
    public void setup() {
        uut = new UserService(userRepository, roleRepository, bCryptPasswordEncoder);
    }

    @Test
    public void testCreateUser() throws UserService.UserNameInvalid {
        UserEntity result = uut.create("user", "first", "last", "email", "password");
        assertNotNull(result);
        assertEquals("user", result.getUserName());
        assertEquals("first", result.getFirstName());
        assertEquals("last", result.getLastName());
        assertEquals("email", result.getEmail());
        assertNotEquals("password", result.getPassword());
        assertTrue(bCryptPasswordEncoder.matches("password", result.getPassword()));
        assertEquals("USER", result.getRoles().iterator().next().getRoleName());
        assertEquals(1, result.getRoles().size());
    }
}