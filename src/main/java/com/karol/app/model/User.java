package com.karol.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@NoArgsConstructor
@Data
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false, unique = true, updatable = false)
    private String mail;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "passenger", orphanRemoval = true)
    private Collection<Booking> bookings;

    public User(String firstName, String lastName, Role role, String mail, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.mail = mail;
        this.password = password;
    }
}
