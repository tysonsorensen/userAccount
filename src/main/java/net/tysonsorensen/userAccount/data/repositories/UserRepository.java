package net.tysonsorensen.userAccount.data.repositories;

import net.tysonsorensen.userAccount.data.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByUserName(String userName);
}
