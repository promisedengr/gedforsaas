package ma.project.GedforSaas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ma.project.GedforSaas.model.Permission;
import ma.project.GedforSaas.service.PermissionService;

import java.util.List;

@RestController
@RequestMapping("admin/api/v1/permission")
public class PermissionAdminController {
    @Autowired
    private PermissionService permissionService;


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
