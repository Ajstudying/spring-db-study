package com.kaj.myapp.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AuthProfile {
    private long id;          // 프로필 id //이게 컨텍트 컨트롤러의 ownerid가 되는 것.
    private String nickname; // 프로필 별칭
    private String username; // 로그인 사용자 이름
}
