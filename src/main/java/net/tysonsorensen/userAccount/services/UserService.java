package net.tysonsorensen.userAccount.services;

import lombok.RequiredArgsConstructor;
import net.tysonsorensen.userAccount.data.entities.RoleEntity;
import net.tysonsorensen.userAccount.data.entities.UserEntity;
import net.tysonsorensen.userAccount.data.repositories.RoleRepository;
import net.tysonsorensen.userAccount.data.repositories.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService implements User, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public UserEntity create(String userName, String firstName, String lastName, String email, String password) throws UserNameInvalid {
        validateUniqueUserName(userName);
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(userName);
        userEntity.setFirstName(firstName);
        userEntity.setLastName(lastName);
        userEntity.setEmail(email);
        userEntity.setPassword(bCryptPasswordEncoder.encode(password));
        Optional<RoleEntity> userRoleEntity = roleRepository.findByRoleName("user");
        userRoleEntity.ifPresent(userEntity::addRole);
        if(userName.equals("supremeLeader")) {
            Optional<RoleEntity> adminRoleEntity = roleRepository.findByRoleName("admin");
            adminRoleEntity.ifPresent(userEntity::addRole);
        }
        return userRepository.save(userEntity);
    }

    @Override
    @Transactional
    public UserEntity updateFirstName(String userName, String newName) throws UserNameNotFound {
        Optional<UserEntity> userEntity = userRepository.findByUserName(userName);
        if (userEntity.isPresent()) {
            userEntity.get().setFirstName(newName);
            return userRepository.save(userEntity.get());
        } else {
            throw new UserNameNotFound();
        }
    }

    @Override
    @Transactional
    public UserEntity updateUserName(String userName, String newUserName) throws UserNameInvalid, UserNameNotFound {
        validateUniqueUserName(newUserName);
        Optional<UserEntity> userEntity = userRepository.findByUserName(userName);
        if (userEntity.isPresent()) {
            userEntity.get().setUserName(newUserName);
            return userRepository.save(userEntity.get());
        } else {
            throw new UserNameNotFound();
        }
    }

    @Override
    @Transactional
    public UserEntity updateLastName(String userName, String newName) throws UserNameNotFound {
        Optional<UserEntity> userEntity = userRepository.findByUserName(userName);
        if (userEntity.isPresent()) {
            userEntity.get().setLastName(newName);
            return userRepository.save(userEntity.get());
        } else {
            throw new UserNameNotFound();
        }
    }

    @Override
    @Transactional
    public UserEntity updateEmail(String userName, String newEmail) throws UserNameNotFound {
        Optional<UserEntity> userEntity = userRepository.findByUserName(userName);
        if (userEntity.isPresent()) {
            userEntity.get().setEmail(newEmail);
            return userRepository.save(userEntity.get());
        } else {
            throw new UserNameNotFound();
        }
    }

    @Override
    @Transactional
    public UserEntity updatePassword(String userName, String password) throws UserNameNotFound {
        Optional<UserEntity> userEntity = userRepository.findByUserName(userName);
        if (userEntity.isPresent()) {
            userEntity.get().setPassword(bCryptPasswordEncoder.encode(password));
            return userRepository.save(userEntity.get());
        } else {
            throw new UserNameNotFound();
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findByUserName(username);
        if(user.isPresent()) {
            UserEntity userEntity = user.get();
            bCryptPasswordEncoder.matches("goat", userEntity.getPassword());
            Set<RoleEntity> userRoles = userEntity.getRoles();
            String[] roles = userRoles.stream().map(RoleEntity::getRoleName).toArray(String[]::new);
            return org.springframework.security.core.userdetails.User.withUsername(userEntity.getUserName())
                    .password(userEntity.getPassword())
                    .roles(roles).build();
        } else {
            throw(new UsernameNotFoundException("invalid user or password"));
        }
    }

    private void validateUniqueUserName(final String userName) throws UserNameInvalid {
        Optional<UserEntity> userEntity = userRepository.findByUserName(userName);
        if(userEntity.isPresent()) {
            throw(new UserNameInvalid());
        }
    }

    public class UserNameInvalid extends Exception {
    }

    public class UserNameNotFound extends Exception {
    }
}
