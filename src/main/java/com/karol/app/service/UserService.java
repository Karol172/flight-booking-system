package com.karol.app.service;

import com.karol.app.model.User;

import java.util.Collection;

public interface UserService {

    Collection<User> getAllUsers ();

    User getUserById (long id);

    User createUser (User user);

    User editUserById (long id, User user, String principalName);

    boolean removeUserById(long id);

    boolean hasAccess (String username, long itemId);

    boolean isAdmin (long id);
}
