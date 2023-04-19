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
@RequestMapping(path = "api/v1/user")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/all")
	public List<User> getAllUsers() {

		return userService.getAllUsers();
	}

	@GetMapping("/find/{id}")
	public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
	}

	@GetMapping("/findByEmail/{email}")
	public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
		return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
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
		return new ResponseEntity<>(userService.deleteUser(id), HttpStatus.OK);
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

	@GetMapping("/findByCriteria/{companyId}/{user}")
	public List<User> findByCriteria(@PathVariable String user,@PathVariable Long companyId) {
		return userService.findByCriteria(user, companyId);
	}


	@GetMapping("id/{id}")
	public User findUserById(@PathVariable Long id) {
		return userService.findUserById(id);
	}
}
