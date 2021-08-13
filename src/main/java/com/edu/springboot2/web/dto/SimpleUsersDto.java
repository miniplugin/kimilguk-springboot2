package com.edu.springboot2.web.dto;

import com.edu.springboot2.domain.simple_users.SimpleUsers;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter @Setter
public class SimpleUsersDto {
    private Long id;
    private String username;
    private String password;
    private String role;
    private Boolean enabled;
    private LocalDateTime modifiedDate;

    //조회
    public SimpleUsersDto(SimpleUsers entity){
        this.id = entity.getId();
        this.username = entity.getUsername();
        this.password = entity.getPassword();
        this.role = entity.getRole();
        this.enabled = entity.getEnabled();
        this.modifiedDate = entity.getModifiedDate();
    }

    //저장,수정
    @Builder
    public SimpleUsersDto(String username, String password, String role, Boolean enabled) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
    }

    //DB 저장 후 PK 값 반환 가능
    public SimpleUsers toEntity(){
        String encPassword = null;
        if(!password.isEmpty()) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            encPassword = passwordEncoder.encode(password);
        }
        return SimpleUsers.builder()
                .username(username)
                .password(encPassword)
                .role(role)
                .enabled(enabled)
                .build();
    }

    @Override
    public String toString() {
        return "SimpleUsersDto{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", enabled=" + enabled +
                ", modifiedDate=" + modifiedDate +
                '}';
    }
}
