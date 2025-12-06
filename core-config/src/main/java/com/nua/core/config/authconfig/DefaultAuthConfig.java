package com.nua.core.config.authconfig;

import com.nua.core.base.dto.CustomUserDetails;
import com.nua.core.base.entities.NUAUserBase;
import com.nua.core.base.interfaces.UsersInterface;
import com.nua.core.exceptions.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DefaultAuthConfig {

    private static final String BLUE = "\u001B[34m";
    private static final String RESET = "\u001B[0m";
    private static final Logger log = LoggerFactory.getLogger(DefaultAuthConfig.class);

    @Bean
    @ConditionalOnMissingBean(UserDetailsService.class)
    UserDetailsService userDetailsService(UsersInterface<? extends NUAUserBase> usersService) {
        log.info(BLUE + "USER DETAILS | CORE - CONFIG" + RESET);
        return username -> usersService.findByUser(username)
                .map(CustomUserDetails::build)
                .orElseThrow(() -> new NotFoundException(NotFoundException.RECORD_NO_FOUND_MESSAGE));
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationManager.class)
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        log.info(BLUE + "AUTH MANAGER | CORE-CONFIG" + RESET);
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }


    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        log.info(BLUE + "PASS ENCODER | CORE-CONFIG" + RESET);
        return new BCryptPasswordEncoder();
    }


}