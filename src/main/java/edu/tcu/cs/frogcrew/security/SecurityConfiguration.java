// src/main/java/edu/tcu/cs/frogcrew/security/SecurityConfiguration.java
package edu.tcu.cs.frogcrew.security;

import edu.tcu.cs.frogcrew.user.FrogCrewUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Value("${api.endpoint.base-url}")
    private String baseUrl;                    //   "/api/v1"

    private final FrogCrewUserService userDetailsService;

    public SecurityConfiguration(FrogCrewUserService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
          .csrf(csrf -> csrf.disable())
          .authorizeHttpRequests(auth -> auth

              /*  ───── PUBLIC ENDPOINTS ─────  */
              .requestMatchers(HttpMethod.POST, baseUrl + "/crewMember").permitAll()   // open registration
              .requestMatchers(HttpMethod.GET , baseUrl + "/invites/**").permitAll()   // invite lookup by token

              /*  ───── ADMIN-ONLY ENDPOINTS ─────  */
              .requestMatchers(HttpMethod.POST, baseUrl + "/invites").hasRole("ADMIN")

              /*  ───── EVERYTHING ELSE ─────  */
              .anyRequest().authenticated()
          )
          .userDetailsService(userDetailsService)
          .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
