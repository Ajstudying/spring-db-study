package com.kaj.mypet.auth;

import com.kaj.mypet.auth.entity.Profile;
import com.kaj.mypet.auth.entity.User;
import com.kaj.mypet.auth.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
import java.util.List;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    JwtUtil jwt;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception{
        System.out.println(request);
        System.out.println(response);
        System.out.println(handler);
        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();

            Auth auth = method.getAnnotation(Auth.class);

            if(auth == null){
                return true;
            }

            String token = request.getHeader("Authorization");
            if(token == null || token.isEmpty()){
                response.setStatus(401);
                return false;
            }
            User user = jwt.validateToken(token.replace("Bearer ", ""));
            System.out.println(user);
            if(user == null){
                response.setStatus(401);
                return false;
            }
            request.setAttribute("user", user);
            return true;

        }
        return true;
    }

}