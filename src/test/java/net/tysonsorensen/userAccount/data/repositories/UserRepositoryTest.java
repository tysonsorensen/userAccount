package net.tysonsorensen.userAccount.data.repositories;

import net.tysonsorensen.userAccount.data.entities.RoleEntity;
import net.tysonsorensen.userAccount.data.entities.UserEntity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private RoleEntity roleEntity;
    private UserEntity userEntity;

    private final String TEST = "TEST";
    private final String PASSWORD = "PASSWORD";
    private final String FIRST = "FIRST";
    private final String LAST = "LAST";
    private final String EMAIL = "EMAIL";

    @Before
    public void setup() {
        roleEntity = new RoleEntity();
        roleEntity.setRoleName(TEST);
        this.entityManager.persist(roleEntity);

        userEntity = new UserEntity();
        userEntity.setUserName(TEST);
        userEntity.setPassword(PASSWORD);
        userEntity.setFirstName(FIRST);
        userEntity.setLastName(LAST);
        userEntity.setEmail(EMAIL);
        userEntity.addRole(roleEntity);
        this.entityManager.persist(userEntity);
    }

    @After
    public void cleanUp() {
        this.entityManager.remove(userEntity);
        this.entityManager.remove(roleEntity);
    }

    @Test
    public void test_findUserByUserName_found() {
        Optional<UserEntity> userEntity = userRepository.findByUserName(TEST);
        assertTrue(userEntity.isPresent());
        assertEquals(TEST, userEntity.get().getUserName());
        assertEquals(PASSWORD, userEntity.get().getPassword());
        assertEquals(FIRST, userEntity.get().getFirstName());
        assertEquals(LAST, userEntity.get().getLastName());
        assertEquals(EMAIL, userEntity.get().getEmail());
        assertTrue(userEntity.get().getRoles().contains(roleEntity));
    }

    @Test
    public void test_findUserByUserName_Notfound() {
        Optional<UserEntity> userEntity = userRepository.findByUserName("NOT_FOUND");
        assertFalse(userEntity.isPresent());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void test_createUser_failUniqueName() {
        Optional<UserEntity> userEntity = userRepository.findByUserName(TEST);
        assertTrue(userEntity.isPresent());
        UserEntity newUser = new UserEntity();
        newUser.setUserName(TEST);
        userRepository.save(newUser);
        userRepository.findByUserName(TEST);
    }

}