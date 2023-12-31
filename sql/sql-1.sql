-- 해당 스키마(데이터베이스)를 사용하겠다.
-- 데이터 베이스
-- : 데이터베이스 객체를 사용할 수 있는 공간
-- : 객체의 종류: 테이블, 뷰, 인덱스, 저장 프로시저
/*
데이터베이스 엔진: 스키마
데이터베이스(스키마) : myapp
Create SCHEMA myapp;
Create DATABASE myapp;
MySQL에서는 스키마와 데이터베이스가 동일한 개념이다.
*/
use myapp;
-- 테이블: 데이터를 저장할 수 있는 기본적인 공간
/* create table 테이블(
   컬럼명 데이터타입 제약조건,
   ....,
   추가적인 제약조건(constraint)
)
*/
/*
 DDL(data definition language) 데이터 정의 언어
 create ...
 SQL(Structured Query Language): 시퀄 구조화 조회 언어
 SEQUEL(... english)
*/
create table contact (
-- varchar: variable charactor
-- 가변문자열, 255byte까지 넣을 수 있음.
email varchar(255) not null, image varchar(255), name varchar(255) not null, phone varchar(255) not null, primary key (email)) engine=InnoDB;

select length('홍'); -- 3byte
select length('a'); -- 1byte

-- insert into 테이블 value(값 목록...);
-- 값 목록은 순서를 잘 맞춰야 함.
insert into contact
value("hong@gmail.com", null, "홍길동", "010-1234-5678");
-- 1 row(s) affected(1 행이 영향을 받았다.)
-- 데이터 1건을 row(행) 또는 record(레코드)
-- 데이터에 대한 속성을 column(열)또는 field(필드)
-- insert into 테이블(칼럼명목록...) values (값목록...)
-- insert into contact(name, phone, email, image)
-- value("John doe", "010-0987-6543", "john@naver.com", null);
insert into contact(name, phone, email, image)
value("John doe", "010-0987-6543", "john@naver.com", null);
/* 기존에 있는 email로 insert
Error Code: 1062. Duplicate entry 'john@naver.com' for key 'contact.PRIMARY'	0.000 sec
*/
/*
주된/기본적인/주요한 키
Primary Key: 제약조건:
1. 테이블 내의 데이터 중에서 같은 값이 중복이 있으면 안됨.
2. null 값이 될 수 없음.
*/
-- 목록 조회
select * from contact;

-- 특정 칼럼으로 정렬하여 조회
-- asc(기본값): 순정렬, desc: 역정렬
select * from contact order by name;

-- 데이터베이스의 PK(index, clustered)
-- clustered(다발) index에 맞게 데이터가 정렬 되어 있음.
-- index(binary tree) 이진법 트리구조이고, 데이터(linked list) 구조이다.
-- index(목차) 트리를 탐색하여 데이터까지 찾아감.
-- PK 값으로 1건만 찾아옴.
-- where 조건식
-- where 컬럼명 = '컬럼값'
select * from contact where eboardboardboardboardcontactmail = 'hong@gmail.com';
select * from contact;

-- 조건에 맞는 레코드 삭제
-- where 절의 조건식에는 PK컬럼 기준으로 나오는게 좋음
-- 실수로 불필요한 레코드가 지워지는 것을 방지할 수 있다. (아래아래 줄)
delete from contact where email='john@naver.com';
-- delete from contact where name='John doe'; 

-- 조건에 맞는 대상 사람들을 지움 ↓
delete from contact where email in(
select email from contact where name > '강');

-- DML(Data Mainpulation(조작) Language): insert, delete
-- 테이블의 데이터 전체 삭제
-- 테이블 구조를 재생성(DDL): truncate
-- truncate는 transaction 로그를 쌓지 않음(복구 불가)
truncate table contact;

create table post (
no bigint not null auto_increment, content varchar(255), created_time bigint not null, creator_name varchar(255), image varchar(255), title varchar(255), primary key (no)) engine=InnoDB;

insert into post (creator_name, created_time, title, content, image) value("Lala", 2, "lorem", "content lorem....","");
insert into post (creator_name, created_time, title, content, image) value("marine", 3, "lorem", "content lorem....","");





select * from post;

select * from post order by no;

delete from post where no='2';

truncate table post;





