package ma.project.GedforSaas.security.config;

import ma.project.GedforSaas.security.filter.JwtAuthenticationFilter;
import ma.project.GedforSaas.security.filter.JwtAutorisationFilter;
import ma.project.GedforSaas.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtAutorisationFilter jwtAutorisationFilter;
    @Autowired
    private PasswordEncoderConfig passwordEncoderConfig;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(this.passwordEncoderConfig.passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("**").permitAll()
//                .antMatchers("/api/v1/authentication/**").permitAll()
//                .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
//                .antMatchers("admin/**").hasAuthority("ROLE_ADMIN")
//                .antMatchers("/superAdmin/**").hasAuthority("ROLE_SUPER_ADMIN")
//                .antMatchers("/user/**").hasAuthority("ROLE_USER")
                .anyRequest().authenticated();
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilter(new JwtAuthenticationFilter(authenticationManager()));
        http.addFilterBefore(jwtAutorisationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
