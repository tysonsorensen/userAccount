package net.tysonsorensen.userAccount.services;

import net.tysonsorensen.userAccount.data.entities.UserEntity;

public interface User {

    UserEntity create(final String userName, final String firstName, final String lastName, final String email, final String password);
    UserEntity updateUserName(final Integer id, final String userName);
    UserEntity updateFirstName(final Integer id, final String newName);
    UserEntity updateLastName(final Integer id, final String newName);
    UserEntity updateEmail(final Integer id, final String newEmail);
    UserEntity updatePassword(final Integer id, final String password);

}
