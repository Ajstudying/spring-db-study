package com.kaj.myapp.auth;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String nickname;
    private String email;

    //관계 맵핑
    @OneToOne
    // profile_id 컬럼이 FK로 생성됨.
    //양방향 참조를 하면 재귀처럼 되어 오류나기 쉽기 때문에 안하는게 낫다.
    private Login login;

    /*
    Login toSaveLogin = Login.builder()
    .username(req.getUsername())
    .password(req.getPassword())
    .build();

    Profile toSavePfofile = Profile.builder()
    .nickname(req.getNickname())
    .email(req.getEmail())
    .build();

    toSaveLogin.setProfile(toSavePfofile);
    repo.save(toSaveLogin);

    return toSavePfofile.getId();
    */

}
