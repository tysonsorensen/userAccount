package net.tysonsorensen.userAccount.data.repositories;

import net.tysonsorensen.userAccount.data.entities.RoleEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RoleRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoleRepository roleRepository;

    @Before
    public void setup() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRoleName("TEST");
        this.entityManager.persist(roleEntity);
    }

    @Test
    public void test_FindRoleByNameFound() {
        Optional<RoleEntity> result = roleRepository.findByRoleName("TEST");
        assertTrue(result.isPresent());
        assertEquals("TEST", result.get().getRoleName());
        assertNotNull(result.get().getId());
    }

    @Test
    public void test_FindRoleByNameNotFound() {
        Optional<RoleEntity> result = roleRepository.findByRoleName("NOT_FOUND");
        assertFalse(result.isPresent());
    }

    @Test
    public void test_createRole() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRoleName("INSERT");
        roleRepository.save(roleEntity);

        Optional<RoleEntity> result = roleRepository.findByRoleName("INSERT");
        assertTrue(result.isPresent());
        assertEquals("INSERT", result.get().getRoleName());
        assertNotNull(result.get().getId());
    }
}