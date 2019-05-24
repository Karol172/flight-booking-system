package com.karol.app.controller;

import com.karol.app.dto.UserDto;
import com.karol.app.model.User;
import com.karol.app.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    private ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity all() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getAllUsers().stream().peek(user -> user.setPassword(null))
                        .map(user -> modelMapper.map(user, UserDto.class))
                        .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity one(@PathVariable("id") long id, Principal principal) {
        if (!userService.hasAccess(principal.getName(), id))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        User user = userService.getUserById(id);
        if (user == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        user.setPassword(null);
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(user, UserDto.class));
    }

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid UserDto userDto) {
        User user = userService.createUser(modelMapper.map(userDto, User.class));
        if (user == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        user.setPassword(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(user, UserDto.class));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity update(@PathVariable("id") long id, @RequestBody @Valid UserDto userDto, Principal principal) {
        if (!userService.hasAccess(principal.getName(), id))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        User user = userService.editUserById(id, modelMapper.map(userDto, User.class), principal.getName());
        if (user == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        user.setPassword(null);
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(user, UserDto.class));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity remove(@PathVariable("id") long id) {
        if (userService.isAdmin(id))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if (!userService.removeUserById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return  ResponseEntity.status(HttpStatus.OK).build();
    }
}
