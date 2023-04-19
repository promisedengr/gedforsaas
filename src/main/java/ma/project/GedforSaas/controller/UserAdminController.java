package ma.project.GedforSaas.controller;

import freemarker.template.TemplateException;
import ma.project.GedforSaas.model.User;
import ma.project.GedforSaas.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
@RestController
@RequestMapping(path = "admin/api/v1/user")
public class UserAdminController {

    @Autowired
    private UserService userService;

    @GetMapping("all")
    public List<User> getAllUsers() {

        return userService.getAllUsers();
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @GetMapping("/findByEmail")
    public ResponseEntity<User> getUserByEmail(@RequestBody User user) {
        return new ResponseEntity<>(userService.getUserByEmail(user.getEmail()), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.addNewUser(user), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateUserById(@PathVariable("id") Long id, @RequestBody User user) {

        return new ResponseEntity<>(userService.updateUserById(id, user), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateUserByEmail(@RequestBody User user) {

        return new ResponseEntity<>(userService.updateUserByEmail(user), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<Object> updatePassword(@RequestBody User user) {

        return new ResponseEntity<>(userService.updatePasswordByEmail(user), HttpStatus.OK);
    }

    @PostMapping("/sendMailToUpdatePassword")
    public String sendMailToUpdatePassword(@RequestBody User user)
            throws IOException, TemplateException, MessagingException {
        return userService.sendMailToUpdatePassword(user);
    }

}
