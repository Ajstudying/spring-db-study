package com.kaj.mypet.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String petname;
    private String species;

    @ManyToOne
    @JoinColumn(name="user_nickname")
    private User user;


}
