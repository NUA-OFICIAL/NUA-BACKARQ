package com.nua.core.security;

import com.nua.core.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String BLUE = "\u001B[34m";
    private static final String RESET = "\u001B[0m";

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws IOException, ServletException {

        logger.info(BLUE + "doInternalFilter" + RESET);
        try {
            String jwtToken = null;
            Cookie refreshToken = null;

            if (request.getServletPath().contains("/login")){
                chain.doFilter(request, response);
                return;
            }

            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if ("jwt".equals(cookie.getName())) jwtToken = cookie.getValue();
                    if ("ref".equals(cookie.getName())) refreshToken = cookie;
                }
            }

            request.setAttribute("jwtToken", jwtToken);
            request.setAttribute("refreshToken", refreshToken);
            logger.info(BLUE + "try doIF" + RESET);

            String user;
            user = jwtUtil.extractUsername(jwtToken);
            final UserDetails userDetails = this.userDetailsService.loadUserByUsername(user);
            final var authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
            chain.doFilter(request, response);
        } catch (Exception e) {
            logger.info(BLUE + "ups! catch" + RESET);
            logger.error(String.valueOf(e));
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"No autorizado\"}");
        }
    }

}