package com.karol.app.service;

import com.karol.app.model.User;

import java.util.Collection;

public interface UserService {

    Collection<User> getAllUsers ();

    User getUserById (long id);

    long createUser (User user);

    boolean editUserById (Long id, User user);

    boolean removeUserById(long id);
}
