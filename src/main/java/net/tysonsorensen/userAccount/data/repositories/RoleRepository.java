package net.tysonsorensen.userAccount.data.repositories;

import net.tysonsorensen.userAccount.data.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {

    Optional<RoleEntity> findByRoleName(final String roleName);
}
