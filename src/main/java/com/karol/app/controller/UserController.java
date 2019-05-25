package com.karol.app.controller;

import com.karol.app.dto.UserDto;
import com.karol.app.model.User;
import com.karol.app.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
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
    @ApiOperation("Get all users")
    public ResponseEntity all() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getAllUsers().stream().peek(user -> user.setPassword(null))
                        .map(user -> modelMapper.map(user, UserDto.class))
                        .collect(Collectors.toList()));
    }

    @GetMapping("/sort/{sortedBy}/page/{page}/{records}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation("Get all users, sort and divide into certain number of pages")
    public ResponseEntity sortAndPaginate (@PathVariable("sortedBy") @ApiParam("Name of the sorting field") String field,
                                           @PathVariable("page") @ApiParam("Page number") int page,
                                           @PathVariable("records") @ApiParam("Number of records on a single page") int records) {
        if (page < 0 || records < 1)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        Page<User> userPage= userService.getAllUsersSortedBy(field, page, records);

        if (userPage == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userPage.stream().peek(user -> user.setPassword(null))
                        .map(airport -> modelMapper.map(airport, UserDto.class))
                        .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @ApiOperation("Get user")
    public ResponseEntity one(@PathVariable("id") @ApiParam("Id of user") long id, Principal principal) {
        if (!userService.hasAccess(principal.getName(), id))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        User user = userService.getUserById(id);
        if (user == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        user.setPassword(null);
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(user, UserDto.class));
    }

    @PostMapping
    @ApiOperation("Create new user")
    public ResponseEntity create(@RequestBody @Valid @ApiParam("User") UserDto userDto) {
        User user = userService.createUser(modelMapper.map(userDto, User.class));
        if (user == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        user.setPassword(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(user, UserDto.class));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @ApiOperation("Update user")
    public ResponseEntity update(@PathVariable("id") @ApiParam("Id of user") long id,
                                 @RequestBody @Valid @ApiParam("User") UserDto userDto, Principal principal) {
        if (!userService.hasAccess(principal.getName(), id))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        User user = userService.editUserById(id, modelMapper.map(userDto, User.class), principal.getName());
        if (user == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        user.setPassword(null);
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(user, UserDto.class));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation("Delete user")
    public ResponseEntity remove(@PathVariable("id") @ApiParam("Id of user") long id) {
        if (userService.isAdmin(id))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if (!userService.removeUserById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return  ResponseEntity.status(HttpStatus.OK).build();
    }

}
