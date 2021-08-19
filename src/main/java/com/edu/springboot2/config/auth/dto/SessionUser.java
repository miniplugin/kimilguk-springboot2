package com.edu.springboot2.config.auth.dto;

import com.edu.springboot2.domain.user.Users;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {

    private String name;
    private String email;
    private String picture;
    private String role;

    public SessionUser(Users user){
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
        this.role = user.getRoleKey();
    }
}
