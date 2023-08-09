package com.kaj.mypet.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Builder
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String petname;
    private String species;
    private String nickname;

    @ManyToOne
    @JoinColumn(name="user_nickname")
    private User user;

    public String getUserNickname() {
        return user.getNickname();
    }

}
