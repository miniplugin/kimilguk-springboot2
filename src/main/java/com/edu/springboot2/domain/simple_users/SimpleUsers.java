package com.edu.springboot2.domain.simple_users;

import com.edu.springboot2.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class SimpleUsers extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private Boolean enabled;

    @Builder
    public SimpleUsers(String username, String password, String role, Boolean enabled){
        this.username = username;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
    }
    //수정시 DB 쿼리 없이 아래 메서드로 DB 데이터 바로 수정 가능
    public void update(String username, String password, String role, Boolean enabled){
        String encPassword = null;
        if(!password.isEmpty()) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            encPassword = passwordEncoder.encode(password);
        }
        this.username = username;
        if(!password.isEmpty()) {
            this.password = password;
        }
        this.role = role;
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "SimpleUsers{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
