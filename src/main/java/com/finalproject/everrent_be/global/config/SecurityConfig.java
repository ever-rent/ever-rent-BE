package com.finalproject.everrent_be.global.config;

import com.finalproject.everrent_be.domain.auth.service.AuthService;
import com.finalproject.everrent_be.global.common.Reissue;
import com.finalproject.everrent_be.global.jwt.JwtAccessDeniedHandler;
import com.finalproject.everrent_be.global.jwt.JwtAuthenticationEntryPoint;
import com.finalproject.everrent_be.global.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final TokenProvider tokenProvider;
    private final Reissue reissue;
    private final CorsConfigurationSource corsConfigurationSource;
    private final RedisTemplate redisTemplate;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // h2 database 테스트가 원활하도록 관련 API 들은 전부 무시
    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("/h2-console/**", "/favicon.ico","/swagger-ui.html/**");
    }

    private static final String[] PERMIT_URL_SWAGGER = {
            /* swagger v2 */
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            /* swagger v3 */
            "/v3/api-docs/**",
            "/swagger-ui/**",
    };
    private static final String[] PERMIT_URL_ARRAY = {
            "/members/**",
            "/products/**",
            "/products",
            "/categories/**",
            "/mailConfirms",
            "/searchs",
            "/wss",
            "/wss/chat/**",
            "/pwChanges",
            "/logins",
            "/signups","/idchecks","/emailchecks",
            "/updateInfo/**",
            "/ratings/{memberId}",
            "/pwMailConfirms",
            "/oauth","/oauth/**"
    };
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().configurationSource(corsConfigurationSource);
        // CSRF 설정 Disable
        http.csrf().disable()

                // exception handling 할 때 우리가 만든 클래스를 추가
                .exceptionHandling()
                //.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                //.accessDeniedHandler(jwtAccessDeniedHandler)

                // h2-console 을 위한 설정을 추가
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                // 시큐리티는 기본적으로 세션을 사용
                // 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 로그인, 회원가입 API 는 토큰이 없는 상태에서 요청이 들어오기 때문에 permitAll 설정
                .and()
                .authorizeRequests()
                .antMatchers(PERMIT_URL_SWAGGER).permitAll()
                .antMatchers(PERMIT_URL_ARRAY).permitAll()
                .anyRequest().authenticated()

                .and()
                .oauth2Login()

                // JwtFilter 를 addFilterBefore 로 등록했던 JwtSecurityConfig 클래스를 적용
                .and()
                .apply(new JwtSecurityConfig(tokenProvider,reissue));
    }
}
