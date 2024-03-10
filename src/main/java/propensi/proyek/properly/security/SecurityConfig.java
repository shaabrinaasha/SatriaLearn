package propensi.proyek.properly.security;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.PasswordGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain chain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((customizer) -> customizer
                        .requestMatchers("/kelas/matpel/**").hasAnyRole("siswa", "guru")
                        .requestMatchers("/kelas/siswa-view").hasRole("siswa")
                        .requestMatchers("/kelas/**").hasRole("admin")
                        .requestMatchers("/semester/**").hasRole("admin")
                        // .requestMatchers("/static/**").permitAll()
                        // .requestMatchers("**.css").permitAll()
                        // .requestMatchers("**.js").permitAll()
                        // .anyRequest().permitAll()
                        // .authenticated()
                        .requestMatchers("/static/**", "**.css", "**.js", "/error", "/login").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .formLogin((customizer) -> customizer
                        .loginPage("/login")
                        .loginProcessingUrl("/login"))
                .logout(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CharacterRule pwdGenerator() {
        CharacterRule rule = new CharacterRule(new CharacterData() {

            @Override
            public String getErrorCode() {
                return "SAMPLE_ERROR_CODE";
            }

            @Override
            public String getCharacters() {
                return "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890!.";
            }

        });

        return rule;
    }

}