package net.tysonsorensen.userAccount.services;

import net.tysonsorensen.userAccount.data.entities.RoleEntity;
import net.tysonsorensen.userAccount.data.entities.UserEntity;
import net.tysonsorensen.userAccount.data.repositories.RoleRepository;
import net.tysonsorensen.userAccount.data.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    private final String USER = "user";
    private final String ADMIN = "admin";
    private final String FIRST = "first";
    private final String LAST = "last";
    private final String EMAIL = "email";
    private final String PASSWORD = "password";
    private final String DUPLICATE = "duplicate";
    private final String LOGIN = "login";
    private UserRepository userRepository = mock(UserRepository.class);
    private RoleRepository roleRepository = mock(RoleRepository.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private UserService uut;

    @Before
    public void setup() throws UserService.UserNameInvalid {
        RoleEntity userRole = new RoleEntity();
        userRole.setRoleName(USER);
        RoleEntity adminRole = new RoleEntity();
        adminRole.setRoleName(ADMIN);
        doReturn(Optional.of(userRole)).when(roleRepository).findByRoleName(USER);
        doReturn(Optional.of(adminRole)).when(roleRepository).findByRoleName(ADMIN);

        UserEntity entity = new UserEntity();
        entity.setUserName(LOGIN);
        entity.setPassword(PASSWORD);
        entity.addRole(userRole);
        doReturn(Optional.of(entity)).when(userRepository).findByUserName(LOGIN);
        doReturn(Optional.of(new UserEntity())).when(userRepository).findByUserName(DUPLICATE);
        uut = new UserService(userRepository, roleRepository, bCryptPasswordEncoder);
    }

    @Test
    public void testCreateUser() throws UserService.UserNameInvalid {
        UserEntity result = uut.create(USER, FIRST, LAST, EMAIL, PASSWORD);
        assertNotNull(result);
        assertEquals(USER, result.getUserName());
        assertEquals(FIRST, result.getFirstName());
        assertEquals(LAST, result.getLastName());
        assertEquals(EMAIL, result.getEmail());
        assertNotEquals(PASSWORD, result.getPassword());
        assertTrue(bCryptPasswordEncoder.matches(PASSWORD, result.getPassword()));
        assertEquals(USER, result.getRoles().iterator().next().getRoleName());
        assertEquals(1, result.getRoles().size());
    }

    @Test
    public void testCreateSupremeLeader() throws UserService.UserNameInvalid {
        String supremeLeader = "supremeLeader";
        UserEntity result = uut.create(supremeLeader, FIRST, LAST, EMAIL, PASSWORD);
        assertNotNull(result);
        assertEquals(supremeLeader, result.getUserName());
        assertEquals(FIRST, result.getFirstName());
        assertEquals(LAST, result.getLastName());
        assertEquals(EMAIL, result.getEmail());
        assertNotEquals(PASSWORD, result.getPassword());
        assertTrue(bCryptPasswordEncoder.matches(PASSWORD, result.getPassword()));
        List<RoleEntity> roleEntities = new ArrayList<>(result.getRoles());
        roleEntities.sort(Comparator.comparing(RoleEntity::getRoleName));
        assertEquals(ADMIN, roleEntities.get(0).getRoleName());
        assertEquals(USER, roleEntities.get(1).getRoleName());
        assertEquals(2, result.getRoles().size());
    }

    @Test(expected = UserService.UserNameInvalid.class)
    public void testCreateUser_userNameAlreadyExists() throws UserService.UserNameInvalid {
        uut.create(DUPLICATE, FIRST, LAST, EMAIL, PASSWORD);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByUsername_notFound() {
        uut.loadUserByUsername("NOT FOUND");
    }

    @Test
    public void testLoadUserByUsername() {
        UserDetails result = uut.loadUserByUsername(LOGIN);
        assertEquals(LOGIN, result.getUsername());
        assertEquals(PASSWORD, result.getPassword());
        assertTrue(result.getAuthorities() != null);
    }

    @Test(expected = UserService.UserNameNotFound.class)
    public void testUpdateFirstName_notFound() throws UserService.UserNameNotFound {
        uut.updateFirstName("NOT FOUND", FIRST);
    }

    @Test
    public void testUpdateFirstName() throws UserService.UserNameNotFound {
        UserEntity result = uut.updateFirstName(LOGIN, FIRST);
        assertEquals(FIRST, result.getFirstName());
    }

    @Test(expected = UserService.UserNameNotFound.class)
    public void testUpdateLasttName_notFound() throws UserService.UserNameNotFound {
        uut.updateLastName("NOT FOUND", FIRST);
    }

    @Test
    public void testUpdateLastName() throws UserService.UserNameNotFound {
        UserEntity result = uut.updateLastName(LOGIN, LAST);
        assertEquals(LAST, result.getLastName());
    }

    @Test(expected = UserService.UserNameNotFound.class)
    public void testUpdateUserName_notFound() throws UserService.UserNameNotFound, UserService.UserNameInvalid {
        uut.updateUserName("NOT FOUND", USER);
    }

    @Test(expected = UserService.UserNameInvalid.class)
    public void testUpdateUserName_duplicate() throws UserService.UserNameNotFound, UserService.UserNameInvalid {
        uut.updateUserName(LOGIN, DUPLICATE);
    }

    @Test
    public void testUpdateUserName() throws UserService.UserNameNotFound, UserService.UserNameInvalid {
        UserEntity result = uut.updateUserName(LOGIN, USER);
        assertEquals(USER, result.getUserName());
    }

    @Test(expected = UserService.UserNameNotFound.class)
    public void testUpdateEmail_notFound() throws UserService.UserNameNotFound {
        uut.updateEmail("NOT FOUND", EMAIL);
    }

    @Test
    public void testUpdateEmail() throws UserService.UserNameNotFound {
        UserEntity result = uut.updateEmail(LOGIN, EMAIL);
        assertEquals(EMAIL, result.getEmail());
    }

    @Test(expected = UserService.UserNameNotFound.class)
    public void testUpdatePassword_notFound() throws UserService.UserNameNotFound {
        uut.updatePassword("NOT FOUND", EMAIL);
    }

    @Test
    public void testUpdatePassword() throws UserService.UserNameNotFound {
        UserEntity result = uut.updatePassword(LOGIN, PASSWORD);
        assertNotEquals(PASSWORD, result.getPassword());
        assertTrue(bCryptPasswordEncoder.matches(PASSWORD, result.getPassword()));
    }
}