package com.karol.app.dto;

import com.karol.app.model.Role;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

//TODO: can't map password from User
@Data
public class UserDto implements Serializable {

    private Long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private Role role;

    @NotNull
    @Email
    private String mail;

    private String password;
}
