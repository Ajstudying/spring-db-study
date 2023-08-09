package com.kaj.myapp.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Auth {
    //역할(일반 사용자, 골드 사용자, 관리자, 판매 관리자)..
    //@Auth(role="GOLD")
//    public booleanH role();
    public boolean require() default true;
}

