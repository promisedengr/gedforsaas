package ma.project.GedforSaas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ma.project.GedforSaas.model.Role;
import ma.project.GedforSaas.service.RoleService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/v1/profil")
public class RoleController {

	@Autowired
	private RoleService roleService;

	@GetMapping("/all")
	public ResponseEntity<List<Role>> getAllRoles() {
		List<Role> roles = roleService.findAllRoles();
		return new ResponseEntity<>(roles, HttpStatus.OK);
	}

	@GetMapping("/find/{id}")
	public ResponseEntity<Role> getRoleById(@PathVariable("id") Long id) {
		Role role = roleService.findRoleById(id);
		return new ResponseEntity<>(role, HttpStatus.OK);
	}

	@PostMapping("/add")
	public ResponseEntity<Role> addRole(@RequestBody Role role) {
		Role newRole = roleService.addRole(role);
		return new ResponseEntity<>(newRole, HttpStatus.CREATED);
	}

	@PutMapping("/update")
	public ResponseEntity<Role> updateUser(@RequestBody Role role) {
		Role updateRole = roleService.updateRole(role);
		return new ResponseEntity<>(updateRole, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteRole(@PathVariable("id") Long id) {
		roleService.deleteRole(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
