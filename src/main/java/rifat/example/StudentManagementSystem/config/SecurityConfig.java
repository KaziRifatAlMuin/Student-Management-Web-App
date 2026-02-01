package rifat.example.StudentManagementSystem.config;

import rifat.example.StudentManagementSystem.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    
    @Autowired
    private CustomUserDetailsService userDetailsService;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/", "/login", "/css/**", "/js/**").permitAll()
                // Teacher-only endpoints
                .requestMatchers("/teachers/new", "/teachers/edit/**", "/teachers/delete/**").hasRole("TEACHER")
                .requestMatchers("/students/new", "/students/edit/**", "/students/delete/**").hasRole("TEACHER")
                .requestMatchers("/courses/new", "/courses/edit/**", "/courses/delete/**").hasRole("TEACHER")
                .requestMatchers("/departments/new", "/departments/edit/**", "/departments/delete/**").hasRole("TEACHER")
                // Both roles can view
                .requestMatchers("/teachers", "/teachers/view/**").hasAnyRole("STUDENT", "TEACHER")
                .requestMatchers("/students", "/students/view/**").hasAnyRole("STUDENT", "TEACHER")
                .requestMatchers("/courses", "/courses/view/**").hasAnyRole("STUDENT", "TEACHER")
                .requestMatchers("/departments", "/departments/view/**").hasAnyRole("STUDENT", "TEACHER")
                // Student profile - students can edit their own
                .requestMatchers("/profile/**").hasAnyRole("STUDENT", "TEACHER")
                .requestMatchers("/dashboard").hasAnyRole("STUDENT", "TEACHER")
                // Course enrollment for students
                .requestMatchers("/students/enroll/**", "/students/unenroll/**").hasAnyRole("STUDENT", "TEACHER")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );
        
        return http.build();
    }
}
