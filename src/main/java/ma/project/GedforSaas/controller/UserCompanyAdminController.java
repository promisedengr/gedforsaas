package ma.project.GedforSaas.controller;

// import ma.project.GedforSaas.model.Company;
import ma.project.GedforSaas.model.Company;
import ma.project.GedforSaas.model.User;
import ma.project.GedforSaas.model.UserCompany;
import ma.project.GedforSaas.service.UserCompanyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "admin/api/v1/userCompany")

public class UserCompanyAdminController {
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

    @GetMapping("/companies/user/email/{email}")
    public List<Company> getCompanyOfUser(@PathVariable String email) {
        return userCompanyService.getCompanyOfUser(email);
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


    @GetMapping("/users/company/{id}")
    public List<User> getUsersCompany(@PathVariable Long id) {
        return userCompanyService.getUsersCompany(id);
    }

    @PostMapping("/store/company/{username}/")
    public Company storeCompany(@RequestBody Company company, @PathVariable String username) {
        return userCompanyService.storeCompany(company, username);
    }
}
