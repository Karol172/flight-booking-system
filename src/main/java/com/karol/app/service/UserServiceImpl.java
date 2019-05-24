package com.karol.app.service;

import com.karol.app.model.Role;
import com.karol.app.model.User;
import com.karol.app.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    public UserServiceImpl (UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Collection<User> getAllUsers () {
        return userRepository.findAll();
    }

    @Override
    public Page<User> getAllUsersSortedBy(String field, int page, int numberRecord) {
        if (!Arrays.asList("id", "firstName", "lastName", "email", "role").contains(field))
            return null;
        return userRepository.findAll(PageRequest.of(page, numberRecord,
                Sort.by(Sort.Direction.ASC, field)));
    }

    @Override
    public User getUserById(long id) {
        Optional<User> user = userRepository.findById(id);
        return user.isPresent() ? user.get() : null;
    }

    @Override
    public User createUser(User user) {
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setId(null);
        return !userRepository.existsByEmail(user.getEmail()) ? userRepository.save(user) : null;
    }

    @Override
    public User editUserById(long id, User user, String principalName) {
        Optional<User> userFromDb = userRepository.findById(id);
        if (userFromDb.isPresent()) {
            userFromDb.get().setFirstName(user.getFirstName());
            userFromDb.get().setLastName(user.getLastName());
            if (principalName.equals(userFromDb.get().getEmail()))
                userFromDb.get().setPassword(passwordEncoder.encode(user.getPassword()));
            else if (user.getRole() != null)
                userFromDb.get().setRole(user.getRole());
            return userRepository.save(userFromDb.get());
        }
        return null;
    }


    @Override
    public boolean removeUserById(long id){
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean hasAccess(String username, long itemId) {
        Optional<User> user = userRepository.findByEmail(username);
        return user.isPresent() && (user.get().getId() == itemId || user.get().getRole() == Role.ADMIN) ? true : false;
    }

    @Override
    public boolean isAdmin (long id) {
        Optional<User> user = userRepository.findById(id);
        return user.isPresent() && user.get().getRole() == Role.ADMIN;
    }

}
