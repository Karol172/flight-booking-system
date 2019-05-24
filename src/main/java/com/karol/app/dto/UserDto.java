package com.karol.app.dto;

import com.karol.app.model.Role;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class UserDto implements Serializable {

    private Long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private Role role;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Length(min = 6)
    private String password;
}
