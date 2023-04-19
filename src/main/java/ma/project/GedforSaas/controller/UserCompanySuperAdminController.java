package ma.project.GedforSaas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ma.project.GedforSaas.model.UserCompany;
import ma.project.GedforSaas.service.UserCompanyService;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping(path = "superAdmin/api/v1/userCompany")
public class UserCompanySuperAdminController {
    @Autowired
    private UserCompanyService userCompanyService;
    @GetMapping("/user/id/{id}")
    public List<UserCompany> findByUserId(@PathVariable Long id) {
        return userCompanyService.findByUserId(id);
    }

    @GetMapping("/company/id/{id}")
    public List<UserCompany> findByCompanyId(@PathVariable Long id) {
        return userCompanyService.findByCompanyId(id);
    }

    @GetMapping("/role/name/{name}")
    public List<UserCompany> findByRoleName(@PathVariable String name) {
        return userCompanyService.findByRoleName(name);
    }

    @GetMapping("/")
    public List<UserCompany> findAll() {
        return userCompanyService.findAll();
    }

    @PostMapping("/")
    public UserCompany save(@RequestBody UserCompany userCompany) {
        return userCompanyService.save(userCompany);
    }

    @DeleteMapping("/id/{id}")
    public void deleteById(@PathVariable Long id) {
        userCompanyService.deleteById(id);
    }
}
