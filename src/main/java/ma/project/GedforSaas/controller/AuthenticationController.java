package ma.project.GedforSaas.controller;

import ma.project.GedforSaas.model.User;
import ma.project.GedforSaas.service.AuthenticationService;
import ma.project.GedforSaas.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authentication")
public class AuthenticationController {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationService authenticationService;

	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody User userRequest) throws Exception {
		return authenticationService.signIn(userRequest);
	}

	@GetMapping("/reset-password/{email}/{currentPass}/{newPass}")
	public User updatePassword(@PathVariable String email,@PathVariable  String currentPass,@PathVariable  String newPass)throws Exception {
		return authenticationService.updatePassword(email, currentPass, newPass);
	}

	@GetMapping("/verify/{email}/{secret}")
	public User verify(@PathVariable String email,@PathVariable  String secret)throws Exception {
		return authenticationService.verifySecret(email,secret);
	}

	@PostMapping("/lock")
	public String desactivateAccount(@RequestBody User user) {
		return authenticationService.desactivateAccount(user);
	}

	@PostMapping("/twoStep")
	public boolean changeTwoStepStatus(@RequestBody User user) {
		return authenticationService.changeTwoStepStatus(user);
	}
	@PostMapping("/changePasswordEverySixMonths")
	public boolean enableChangePassReminder(@RequestBody User user) {
		return authenticationService.enableChangePassReminder(user);
	}



}
