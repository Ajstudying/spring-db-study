package com.kaj.myapp.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data //getter, setter 등을 자동으로 만들어준다. 컴파일 시점에
@Builder
@AllArgsConstructor //, 기본적인 생성자까지 자동으로 만들어준다. 컴파일 시점에
public class Account {
    private String ano;
    private String ownerName;
    private long balance;
    private long createTime;

}
