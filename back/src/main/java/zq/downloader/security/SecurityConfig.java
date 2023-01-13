package zq.downloader.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserDetailsService service;
    private final PasswordEncoder encoder;
    @Value("${zq.public-path}")
    String path;

    @Value("${zq.enable-security}")
    boolean enableSecurity;

    @Autowired
    public SecurityConfig(UserDetailsService service, PasswordEncoder encoder) {
        this.service = service;
        this.encoder = encoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        if (enableSecurity) {
            return http.csrf().disable().cors().disable().headers().frameOptions().disable()
                    .and().formLogin().loginPage(path + "/login").loginProcessingUrl(path + "/login-process").defaultSuccessUrl(path)
                    .and().logout().logoutUrl(path + "/logout").logoutSuccessUrl(path + "/login").permitAll()
                    .and().authorizeRequests().antMatchers(path, path + "/tasks", path + "/status", path + "/about", path + "/api/**", path + "/info/**", path + "/ws/**").hasAuthority("ROLE_LOGIN")
                    .and().authorizeRequests().antMatchers(path + "/html/**").denyAll()
                    .and().rememberMe().alwaysRemember(true)
                    .and().build();
        } else {
            return http.csrf().disable().cors().disable().headers().frameOptions().disable().and().authorizeHttpRequests().anyRequest().permitAll().and().build();
        }
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(service).passwordEncoder(encoder)
                .and().build();
    }


}
