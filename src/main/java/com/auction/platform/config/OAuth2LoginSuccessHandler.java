package com.auction.platform.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.auction.platform.entity.Role;
import com.auction.platform.entity.User;
import com.auction.platform.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public OAuth2LoginSuccessHandler(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // Check if user exists
        User user = userRepository.findByEmail(email);

        if (user == null) {
            // Auto-register Google user
            user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setRole(Role.BUYER);   // default role
            user.setEnabled(true);
            userRepository.save(user);
        }

        //  Generate JWT
        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        // Redirect to frontend with token
        String redirectUrl =
                "http://localhost:5173/oauth-success?token=" + token +
                "&role=" + user.getRole().name() +
                "&userId=" + user.getId();

        response.sendRedirect(redirectUrl);
    }
}
