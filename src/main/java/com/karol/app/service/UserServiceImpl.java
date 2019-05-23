package com.karol.app.service;

import com.karol.app.model.User;
import com.karol.app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Collection<User> getAllUsers () {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public long createUser(User user) {
        return userRepository.existsByMail(user.getMail()) ? userRepository.save(user).getId() : null;
    }

    @Override
    public boolean editUserById(Long id, User user) {
        Optional<User> userFromDb = userRepository.findById(id);
        if (userFromDb.isPresent()) {
            userFromDb.get().setFirstName(user.getFirstName());
            userFromDb.get().setLastName(user.getLastName());
            userFromDb.get().setRole(user.getRole()); //TODO: To check later
            userRepository.save(userFromDb.get());
            return true;
        }
        return false;
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

}
