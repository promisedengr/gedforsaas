package ma.project.GedforSaas.service;

// import ma.project.GedforSaas.model.Company;
import ma.project.GedforSaas.model.Company;
import ma.project.GedforSaas.model.Role;
import ma.project.GedforSaas.model.User;
import ma.project.GedforSaas.model.UserCompany;
import ma.project.GedforSaas.repository.UserCompanyRepository;
import ma.project.GedforSaas.exception.UserNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserCompanyService {
    @Autowired
    private UserCompanyRepository userCompanyRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private RoleService roleService;
    Map<String, Company> currentCompany = new HashMap<>();


    public List<Company> getCompanyOfUser(String email) {
        return userCompanyRepository.getCompanyOfUser(email);
    }

    public List<UserCompany> findByUserId(Long id) {
        return userCompanyRepository.findByUserId(id);
    }

    public List<UserCompany> findByCompanyId(Long id) {
        return userCompanyRepository.findByCompanyId(id);
    }

    public List<UserCompany> findByRoleName(String name) {
        return userCompanyRepository.findByRoleName(name);
    }

    public List<UserCompany> findAll() {
        return userCompanyRepository.findAll();
    }

    public UserCompany save(UserCompany userCompany) {
        Optional<User> user = this.userService.findById(userCompany.getUser().getId());
        Company company = this.companyService.findCompanyById(userCompany.getCompany().getId());
        Role role = this.roleService.findRoleByName(userCompany.getRole().getName());
        if (user.isPresent()) {
            userCompany.setUser(user.get());
        } else {
            throw new UserNotFoundException("User with email " + userCompany.getUser().getEmail() + " not found");
        }
        userCompany.setCompany(company);
        userCompany.setRole(role);
        return userCompanyRepository.save(userCompany);
    }

    public Optional<UserCompany> findById(Long aLong) {
        return userCompanyRepository.findById(aLong);
    }

    @Transactional
    public void deleteById(Long aLong) {
        userCompanyRepository.deleteById(aLong);
    }

    public UserCompany creatNewUserCompany(UserCompany userCompany) {

        return userCompanyRepository.save(userCompany);
    }

    public UserCompany generateNewUserCompany(User userSavedInDB) {

        // create new company
        Company company = new Company();
        company.setName(userSavedInDB.getEmail());
        company.setAdress(userSavedInDB.getCity());
        company.setPhoneNumber(userSavedInDB.getPhoneNumber());
        // save it in database
        Company companySavedInDB = companyService.addCompany(company);

        // create new instance of relation between user and company
        UserCompany userCompany = new UserCompany(null, userSavedInDB, companySavedInDB, userSavedInDB.getRole());

        // create new user Company
        return creatNewUserCompany(userCompany);
    }

    public List<UserCompany> deleteAllByUserId(Long userId) {
        return userCompanyRepository.deleteAllByUserId(userId);
    }

    public List<User> getUsersCompany(Long id) {
        return userCompanyRepository.getUsersCompany(id);
    }

    public Company getStoredCompany(String username) {
        return currentCompany.get(username);
    }

    public Company storeCompany(Company company, String username) {
        if (currentCompany.containsKey(username)) {
            currentCompany.replace(username, company);
            return currentCompany.get(username);
        } else {
            this.currentCompany.put(username, company);
            return currentCompany.get(username);
        }
    }
}
