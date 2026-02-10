package com.auction.platform.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    public SecurityConfig(
            JwtFilter jwtFilter,
            OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler) {
        this.jwtFilter = jwtFilter;
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            .authorizeHttpRequests(auth -> auth

    /
    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

    .requestMatchers("/api/users/**").permitAll()
    .requestMatchers("/uploads/**").permitAll()
    .requestMatchers("/ws/**").permitAll()
    .requestMatchers("/oauth2/**").permitAll()

    .requestMatchers("/api/bids/place").hasRole("BUYER")
    .requestMatchers("/api/bids/**").hasAnyRole("BUYER","SELLER")
    .requestMatchers("/api/wallet/**").hasAnyRole("BUYER","SELLER","ADMIN")

    .requestMatchers("/api/auctions/**").authenticated()
    .requestMatchers("/api/transactions/**").authenticated()

    .anyRequest().authenticated()
)
            .oauth2Login(oauth -> oauth
                .successHandler(oAuth2LoginSuccessHandler)
            )

            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        
        config.setAllowedOriginPatterns(List.of(
            "http://localhost:*",
            "https://*.vercel.app"
        ));

        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
