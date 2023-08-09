package com.kaj.mypet.auth;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignUpRequest {
    private String userId;
    private String password;
    private String nickname;
    private String petname;
    private String species;

}
