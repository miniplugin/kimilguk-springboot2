package com.edu.springboot2.config.auth;

import com.edu.springboot2.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .csrf().disable().headers().frameOptions().disable().and()
                .authorizeRequests()
                .antMatchers("/simple_users/**").hasRole(Role.ADMIN.name())
                .antMatchers("/api/v1/**","/posts/**").hasAnyRole(Role.USER.name(),Role.ADMIN.name())
                .antMatchers("/login").permitAll()//추가
                .antMatchers("/**","/css/**","/images/**","/js/**","/h2-console/**", "/profile").permitAll()
                //.antMatchers("/api/v1/**").hasRole("USER")
                //.antMatchers("/api/v1/**").permitAll()
                .anyRequest().authenticated()
                .and()//추가 formLogin()
                .formLogin().loginPage("/login").failureUrl("/login?message=error").permitAll()//추가
                .defaultSuccessUrl("/")//추가
                .and()
                .logout().invalidateHttpSession(true)
                .logoutSuccessUrl("/")
                .and()
                .oauth2Login()
                .loginPage("/login")//추가
                .userInfoEndpoint()
                .userService(customOAuth2UserService);

    }
    //인증 매니저(Authentication Manager)가 인증에 대한 실제적 처리를 담당합니다.(아래)
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .rolePrefix("ROLE_")
                .usersByUsernameQuery("select username, replace(password, '$2y', '$2a'), true from simple_users where username = ?")
                .authoritiesByUsernameQuery("select username, role from simple_users where username = ?");

        /*
        auth.inMemoryAuthentication()
                .withUser("admin").password(passwordEncoder().encode("1234")).roles("ADMIN")
                .and()
                .withUser("user").password(passwordEncoder().encode("1234")).roles("USER")
                .and()
                .withUser("guest").password(passwordEncoder().encode("guest")).roles("GUEST");
        */
    }

    // passwordEncoder() 추가
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
