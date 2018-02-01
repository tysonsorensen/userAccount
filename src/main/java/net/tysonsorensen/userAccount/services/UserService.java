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

@Service
@RequiredArgsConstructor
public class UserService implements User, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public UserEntity create(String userName, String firstName, String lastName, String email, String password) {
        validateUniqueUserName(userName);
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(userName);
        userEntity.setFirstName(firstName);
        userEntity.setLastName(lastName);
        userEntity.setEmail(email);
        userEntity.setPassword(bCryptPasswordEncoder.encode(password));
        Optional<RoleEntity> roleEntity = roleRepository.findById(2);
        Optional<RoleEntity> roleEntity2 = roleRepository.findById(1);
        roleEntity2.ifPresent(userEntity::addRole);
        roleEntity.ifPresent(userEntity::addRole);
        return userRepository.save(userEntity);
    }

    @Override
    @Transactional
    public UserEntity updateFirstName(Integer id, String newName) {
        UserEntity userEntity = userRepository.getOne(id);
        userEntity.setFirstName(newName);
        return userRepository.save(userEntity);
    }

    @Override
    @Transactional
    public UserEntity updateUserName(Integer id, String newUserName) {
        validateUniqueUserName(newUserName);
        UserEntity userEntity = userRepository.getOne(id);
        userEntity.setUserName(newUserName);
        return userRepository.save(userEntity);
    }

    @Override
    @Transactional
    public UserEntity updateLastName(Integer id, String newName) {
        UserEntity userEntity = userRepository.getOne(id);
        userEntity.setLastName(newName);
        return userRepository.save(userEntity);
    }

    @Override
    @Transactional
    public UserEntity updateEmail(Integer id, String newEmail) {
        UserEntity userEntity = userRepository.getOne(id);
        userEntity.setEmail(newEmail);
        return userRepository.save(userEntity);
    }

    @Override
    @Transactional
    public UserEntity updatePassword(Integer id, String password) {
        UserEntity userEntity = userRepository.getOne(id);
        userEntity.setPassword(bCryptPasswordEncoder.encode(password));
        return userRepository.save(userEntity);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findByUserName(username);
        if(user.isPresent()) {
            UserEntity userEntity = user.get();
            bCryptPasswordEncoder.matches("goat", userEntity.getPassword());
            return new org.springframework.security.core.userdetails.User(userEntity.getUserName(),
                    userEntity.getPassword(), Collections.singleton(new SimpleGrantedAuthority("admin")));
        } else {
            throw(new UsernameNotFoundException("invalid user or password"));
        }
    }

    private void validateUniqueUserName(final String userName) {
        Optional<UserEntity> userEntity = userRepository.findByUserName(userName);
        if(userEntity.isPresent()) {
            throw(new RuntimeException("Invalid user name"));
        }
    }
}
