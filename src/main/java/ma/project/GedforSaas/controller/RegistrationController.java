package ma.project.GedforSaas.controller;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.security.auth.login.AccountNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import freemarker.template.TemplateException;
import ma.project.GedforSaas.model.User;
import ma.project.GedforSaas.service.RegistrationService;
import ma.project.GedforSaas.service.UserService;

@RestController
@RequestMapping(path = "api/v1/registration")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private UserService userService;

    @PostMapping("/send")
    public ResponseEntity<Object> register(@RequestBody(required = true) User userRequest, HttpServletRequest request)
            throws IOException, TemplateException, MessagingException {

        return new ResponseEntity<>(registrationService.register(userRequest), HttpStatus.CREATED);
    }

    @PostMapping("/confirm")
    public ResponseEntity<Object> confirmEmail(@RequestParam(value = "token", required = true) String token)
            throws Exception {

        return new ResponseEntity<>(registrationService.confirmEmail(token), HttpStatus.OK);
    }

    // register the user by invitation on his email
    @GetMapping("/sendInvitation/{email}/{companyId}")
    public ResponseEntity<Object> sendInvitation(@PathVariable String email,@PathVariable Long companyId)
            throws IOException, TemplateException, MessagingException {
        System.out.println(email);
        return new ResponseEntity<>(registrationService.registerByInvitation(email,companyId), HttpStatus.OK);
    }

    // register the user by invitation on his email
    @PostMapping("/confirmInvitation")
    public ResponseEntity<Object> confirmInvitation(@RequestParam("token") String token) {
        return new ResponseEntity<>(registrationService.confirmInvitation(token), HttpStatus.OK);
    }

}
