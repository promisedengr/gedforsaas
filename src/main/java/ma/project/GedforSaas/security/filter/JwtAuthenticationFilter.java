package ma.project.GedforSaas.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;

import ma.project.GedforSaas.model.User;
import ma.project.GedforSaas.security.config.JwtUtil;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        User user=null;
        try {
        user= new ObjectMapper().readValue(request.getInputStream(), User.class);
            System.out.println("user.getUsername() = " + user.getUsername());
            System.out.println("user.getPassword() = " + user.getPassword());
           return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getUsername(), user.getPassword()));
        } catch (Exception e) {
            throw new RuntimeException("bad credential");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User principal = (User) authResult.getPrincipal();
        String token = new JwtUtil().generateToken(principal);
        response.addHeader(JwtConstant.AUTORIZATION, JwtConstant.BEARER+token);
    }
}
