package com.kaj.myapp.auth;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Login {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //candidate key(후보키라는 뜻. PK가 가능한 키, 유일성+대표성, 시스템에는 적합하지 않음.)
    //시스템의 PK로 사용 적합X
    // clustered-index로 사용하기가 부적합
    @Column(unique = true)
    private String username;
    @Column(length = 500)
    private String password;

//    @OneToOne
//    @JoinColumn(name = "profile_id")
//    //관계 테이블에 키값만 저장
//    private Profile profile;
    private Long profileId;




    // EAGER: JPA 메서드 실행하면 바로 관계 테이블 조회
    // LAZY: 관계 테이블 객체를 엑세스 지점에 SQL 실행해서 조회
//    @OneToOne(fetch = FetchType.LAZY)
    //profile_id의 forein key가 생성
//    @JsonIgnore
    //JSON으로 출력할 때 처리 안함.

}
