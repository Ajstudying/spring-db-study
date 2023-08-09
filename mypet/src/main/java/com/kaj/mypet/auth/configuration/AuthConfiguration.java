package com.kaj.mypet.auth.configuration;

import com.kaj.mypet.auth.util.HashUtil;
import com.kaj.mypet.auth.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfiguration {
    @Bean
    public HashUtil hashUtil(){
        return new HashUtil();
    }
    @Bean
    public JwtUtil jwtUtil(){
        return new JwtUtil();
    }

}