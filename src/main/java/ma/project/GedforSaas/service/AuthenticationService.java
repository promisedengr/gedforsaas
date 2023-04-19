package ma.project.GedforSaas.service;

import lombok.AllArgsConstructor;
import ma.project.GedforSaas.configuration.ConstantFileNames;
import ma.project.GedforSaas.configuration.ConstantStaticStrings;
import ma.project.GedforSaas.exception.UserNotFoundException;
import ma.project.GedforSaas.model.MailComponent;
import ma.project.GedforSaas.model.User;
import ma.project.GedforSaas.repository.UserRepository;
import ma.project.GedforSaas.security.config.JwtUtil;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

import static ma.project.GedforSaas.controller.TranslateController.LOCALE;
import static ma.project.GedforSaas.security.filter.JwtConstant.JWT_TOKEN_HEADER;
import static ma.project.GedforSaas.utils.i18n.Translator.TO_LOCALE;
import static ma.project.GedforSaas.utils.i18n.TranslatorCode.USER_NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Service
@AllArgsConstructor
public class AuthenticationService {

    public ResponseEntity<?> signIn(User user) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getUsername(), user.getPassword()
            ));
        } catch (BadCredentialsException e) {
            System.out.println(e.getMessage());
            throw new UserNotFoundException(e.getMessage());
        }
        User loadUserByUsername = loadUserByUsername(user.getUsername());
        if (loadUserByUsername.isTwoFactorAuth()) {
            String secret = this.generateSecret();
            loadUserByUsername.setSecret(secret);
            this.userDao.save(loadUserByUsername);

//            emailService.send(mail, ConstantFileNames.CONFIRMATION_TEMPLATE_MAIL);
            System.out.println(secret);
            // send the mail to the email
            MailComponent mail = new MailComponent("Secret code : " + secret,
                    ConstantStaticStrings.MAIL_SUBJECT_SECRET_CODE, "Secret code : " + secret, loadUserByUsername.getEmail(), null);
            emailSenderService.sendUsingTemplate(mail, ConstantFileNames.SECRET_TEMPLATE_MAIL);
        }
        MultiValueMap<String, String> jwtHeader = getJwtHeader(user);
        return new ResponseEntity<>(loadUserByUsername, jwtHeader, OK);
    }

    public String generateSecret() {
        return RandomStringUtils.randomNumeric(6);
    }

    public User updatePassword(String email, String currentPass, String newPass) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    email, currentPass
            ));
        } catch (BadCredentialsException e) {
            throw new Exception("bad credited for username " + email);
        }
        User loadUserByUsername = loadUserByUsername(email);
        loadUserByUsername.setPassword(this.passwordEncoder.encode(newPass));
        return this.userDao.save(loadUserByUsername);
    }

    MultiValueMap<String, String> getJwtHeader(User user) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(JWT_TOKEN_HEADER, jwtUtil.generateToken(user));
        MultiValueMap<String, String> headers = new
                LinkedMultiValueMap<>();
        String token = jwtUtil.generateToken(user);
        System.out.println("token: " + token);
        headers.add("Authorization", token);
        headers.add(JWT_TOKEN_HEADER, token);
        headers.add("Content-Type", "application/json");
        return headers;
    }


    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userDao.findByEmail(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException(TO_LOCALE(USER_NOT_FOUND, LOCALE));
        } else {
            return user.get();
        }
    }

    public String automaticAuthenticate(User userFromToken) throws Exception {
        // TODO: manage JWT
        final String jwt = jwtUtil.generateToken(userFromToken);
        return jwt;
    }

    public User verifySecret(String email, String secret) throws Exception {
        User user = this.loadUserByUsername(email);
        System.out.println(secret);
        System.out.println(user.getSecret());
        if (user.getSecret().equals(secret)) {
            return user;
        } else {
            throw new Exception("Secret incorrect");
        }
    }

    public String desactivateAccount(User user) {
        user.setActive(false);
        this.userDao.save(user);
        return "Your account is locked";
    }

    public boolean changeTwoStepStatus(User user) {
        this.userDao.save(user);
        return user.isTwoFactorAuth();
    }

    public boolean enableChangePassReminder(User user) {
        this.userDao.save(user);
        return user.isChangePasswordEverySixMonths();
    }

    //    private Logger LOGGER = LoggerFactory.getLogger(getClass());
    @Autowired
    public JavaMailSender mailSender;
    @Autowired
    private UserRepository userDao;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private EmailSenderService emailSenderService;


}
