package com.kaj.myapp.contact;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor //전체 필드 초기 생성자
@NoArgsConstructor // 빈 생성자
@Entity
@IdClass(ContactId.class) // PK 클래스
// @Entity는 기본적으로 클래스명(파스칼케이스) -> 테이블명 (스네이크케이스) 맵핑
// class: ContactActivity -> table: contact_activity

//ORM(Object Relationship Mapping)
// : 소프트웨어의 객체를 데이터베이스 테이블로 맵핑하는 방법
// - 사용이유
// : 데이터베이스의 의존도를 낮추는 방법
// : 객체지향적인 원래의 소프트웨어 개발방법을 사용하자.(코드위주로 하자는 말이 더 가까움.)

//JPA(Java Persistent API)
// : 내부적으로 Hibernate를 이용하여 구현된 interface를 제공
// : 자바에서 ORM을 처리하는 표준 방법
public class Contact {

    //@Id: 엔티티의 PK(primary key)를 지정
    //Primary Key: 유일한 속성값이며, 모든 속성 값이 PK에 종속됨. 대표성
    // PK: 유일성+대표성이 만족이 되어야 함.

    //PK 칼럼 및 제약조건 설정

    //ownerId + email : 두개를 합쳐서 연락처 PK로 만들 생각
    //key
    @Id
    //연락처 소유자 id(프로필 id)
    private long ownerId;
    //key
    @Id
    private String email; //계정 Id, 인터넷 세계의 집주소(불변)


    //제약 조건, NOT, NULL
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String phone;

    //mysql long text 크기 검색.
    //칼럼의 데이터 타입을 변경
//    @Column(columnDefinition = "LONGTEXT") //MySQL 전용 방법

    // 컬럼크기 1024byte * 1024 = 1mb * 20 = 20mb
    @Column(length = 1024 * 1024 * 20)
    //파일을 base64 data-url 문자열로 저장
    private String image;



//    @Override
//    public int compareTo(Object o) {
//        Contact c = (Contact) o;
//        return  c.getId() - this.getId();
//    }

}
