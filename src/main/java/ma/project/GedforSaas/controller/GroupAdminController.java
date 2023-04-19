package ma.project.GedforSaas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import ma.project.GedforSaas.model.Group;
import ma.project.GedforSaas.service.GroupService;

import java.util.List;
@RestController
@RequestMapping("admin/api/v1/group")
public class GroupAdminController {
    @Autowired
    private GroupService groupService;

    @PostAuthorize("hasPermission(returnObject, 'READ')")
    @GetMapping("company/id/{id}")
    public List<Group> findByCompanyId(@PathVariable Long id) {
        return groupService.findByCompanyId(id);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addGroup(@RequestBody(required = true) Group group) throws Exception {
        return new ResponseEntity<>(groupService.creatNewGroup(group), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public List<Group> getAllGroups() {
        return groupService.findAllGroups();
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Group> getGroupById(@PathVariable("id") Long id) {
        Group group = groupService.findGroupById(id);
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Group> updateGroup(@RequestBody Group group) {
        Group updateGroup = groupService.updateGroup(group);
        return new ResponseEntity<>(updateGroup, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable("id") Long id) {
        groupService.deleteGroup(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}