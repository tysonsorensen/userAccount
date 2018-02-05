package net.tysonsorensen.userAccount.services;

import net.tysonsorensen.userAccount.data.entities.UserEntity;

public interface User {

    UserEntity create(final String userName, final String firstName, final String lastName, final String email, final String password) throws UserService.UserNameInvalid;
    UserEntity updateUserName(final String userNamed, final String userName) throws UserService.UserNameInvalid, UserService.UserNameNotFound;
    UserEntity updateFirstName(final String userName, final String newName) throws UserService.UserNameNotFound;
    UserEntity updateLastName(final String userName, final String newName) throws UserService.UserNameNotFound;
    UserEntity updateEmail(final String userName, final String newEmail) throws UserService.UserNameNotFound;
    UserEntity updatePassword(final String userName, final String password) throws UserService.UserNameNotFound;

}
