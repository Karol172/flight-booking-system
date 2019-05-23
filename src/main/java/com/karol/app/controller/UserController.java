package com.karol.app.controller;

import com.karol.app.dto.UserDto;
import com.karol.app.model.User;
import com.karol.app.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    private ModelMapper modelMapper;

    public UserController (UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public Collection<User> all () {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> index (@PathVariable("id") long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            UserDto userDto = modelMapper.map(user, UserDto.class);
            userDto.setPassword(null);
            return ResponseEntity.status(HttpStatus.OK).body(userDto);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public Long create (@RequestBody @Valid UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        return userService.createUser(user);
    }

/*    @PutMapping("/{id}")
    public boolean edit (@PathVariable("id") long id, @RequestBody @Valid UserDto user) {
        return userService.editUserById(id, user);
    }*/

    @DeleteMapping("/{id}")
    public ResponseEntity remove (@PathVariable("id") long id){
        return userService.removeUserById(id) ?
            ResponseEntity.status(HttpStatus.OK).build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();


    }
}
