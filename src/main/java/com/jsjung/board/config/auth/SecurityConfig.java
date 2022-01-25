package com.jsjung.board.config.auth;

import com.jsjung.board.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity // Spring Security 설정 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable() //h2 console 화면을 사용하기 위해 해당 옵션을 disable
                .and()
                    .authorizeRequests() // URL별 권한 관리를 설정하는 옵션
                    .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2/**").permitAll()
                    .antMatchers("/api/v1/**").hasRole(Role.USER.name()) // USER 권한을 가진 사람만
                    .anyRequest().authenticated() // 설정된 값들 이외 나머지 URL들을 나타낸다. authenticated()을 추가하여 나머지 URL들은 인증된 사용자들에게만 허용
                .and()
                    .logout()
                        .logoutSuccessUrl("/")
                .and()
                    .oauth2Login()
                        .userInfoEndpoint()
                            .userService(customOAuth2UserService);

    }
}
