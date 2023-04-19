package ma.project.GedforSaas.controller;

import java.util.List;

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

import ma.project.GedforSaas.model.Permission;
import ma.project.GedforSaas.service.PermissionService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/v1/permission")
public class PermissionController {

	private final PermissionService permissionService;

	public PermissionController(PermissionService permissionService) {
		this.permissionService = permissionService;
	}

	@GetMapping("/all")
	public ResponseEntity<List<Permission>> getAllPermissions() {
		List<Permission> permissions = permissionService.findAllPermissions();
		return new ResponseEntity<>(permissions, HttpStatus.OK);
	}

	@GetMapping("/find/{id}")
	public ResponseEntity<Permission> getPermissionById(@PathVariable("id") Long id) {
		Permission permission = permissionService.findPermissionById(id);
		return new ResponseEntity<>(permission, HttpStatus.OK);
	}

	@PostMapping("/add")
	public ResponseEntity<Permission> addPermission(@RequestBody Permission permission) {
		Permission newPermission = permissionService.addPermission(permission);
		return new ResponseEntity<>(newPermission, HttpStatus.CREATED);
	}

	@PutMapping("/update")
	public ResponseEntity<Permission> updatePermission(@RequestBody Permission permission) {
		Permission updatePermission = permissionService.updatePermission(permission);
		return new ResponseEntity<>(updatePermission, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deletePermission(@PathVariable("id") Long id) {
		permissionService.deletePermission(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
