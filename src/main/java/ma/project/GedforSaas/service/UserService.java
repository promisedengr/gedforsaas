package ma.project.GedforSaas.service;

import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ma.project.GedforSaas.abstractSupperClasses.AbstractService;
import ma.project.GedforSaas.configuration.ConstantFileNames;
import ma.project.GedforSaas.configuration.ConstantStaticStrings;
// import ma.project.GedforSaas.model.Company;
import ma.project.GedforSaas.model.Company;
import ma.project.GedforSaas.model.GroupUserAssociations;
import ma.project.GedforSaas.model.MailComponent;
import ma.project.GedforSaas.model.User;
import ma.project.GedforSaas.model.UserCompany;
import ma.project.GedforSaas.repository.UserRepository;
import ma.project.GedforSaas.exception.ResourceAlreadyExistCustomized;
import ma.project.GedforSaas.exception.ResourceNotFoundExceptionConstimized;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.security.auth.login.AccountNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ma.project.GedforSaas.controller.TranslateController.LOCALE;
import static ma.project.GedforSaas.utils.i18n.Translator.TO_LOCALE;
import static ma.project.GedforSaas.utils.i18n.TranslatorCode.*;

@AllArgsConstructor
@Service
@NoArgsConstructor
public class UserService extends AbstractService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private EmailSenderService emailService;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AclService aclService;
    @Autowired
    private CompanyService companyService;

    @Autowired
    private GroupService groupService;


    public User getUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExceptionConstimized(TO_LOCALE(ID_NOT_FOUND, LOCALE)));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByEmailIfExiste(String email) throws AccountNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundExceptionConstimized(TO_LOCALE(EMAIL_NOT_FOUND, LOCALE)));
    }

    // used when we need to make sure that the user exist in database
    public User getUserByEmail(String email) {

        Optional<User> user = userRepository.findByEmail(email);

        if (!user.isPresent()) {
            throw new ResourceNotFoundExceptionConstimized(TO_LOCALE(EMAIL_NOT_FOUND, LOCALE));
        }

        return user.get();
    }

    // used when we need to make sure that the user doesnt exist in database
    public void checkIfEmailExistInDB(String email) {

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            if (user.get().isConfirmed()) {
                throw new ResourceAlreadyExistCustomized(TO_LOCALE(EMAIL_ALREADY_EXISTE_AND_CONFIRMED, LOCALE));
            } else {
                throw new ResourceAlreadyExistCustomized(TO_LOCALE(EMAIL_ALREADY_EXISTE, LOCALE));
            }
        }

    }

    // add user if not exist already
    public User addNewUser(User userRequest) {

        checkIfEmailExistInDB(userRequest.getEmail());


        return userRepository.save(userRequest);
    }

    public boolean emailIsPresent(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public User updateUserById(Long id, User userRequest) {

        User userFoundInDB = getUserById(id);
        userFoundInDB.setActive(userRequest.isActive());
        userFoundInDB.setConfirmed(userRequest.isConfirmed());
        userFoundInDB.setEmail(userRequest.getEmail());
        userFoundInDB.setBirthDate(userRequest.getBirthDate());
        userFoundInDB.setCertificat(userRequest.getCertificat());
        userFoundInDB.setFirstName(userRequest.getFirstName());
        userFoundInDB.setLastName(userRequest.getLastName());
        userFoundInDB.setPhoneNumber(userRequest.getPhoneNumber());
        userFoundInDB.setSignature(userRequest.getSignature());
        userFoundInDB.setWorkspace(userRequest.getWorkspace());
        userFoundInDB.setAbout(userRequest.getAbout());
        userFoundInDB.setCity(userRequest.getCity());
        userFoundInDB.setCountry(userRequest.getCountry());
        userFoundInDB.setInformation(userRequest.getInformation());
        userFoundInDB.setLanguage(userRequest.getLanguage());
        userFoundInDB.setTitle(userRequest.getTitle());
        userFoundInDB.setTwoFactorAuth(userRequest.isTwoFactorAuth());
        userFoundInDB.setChangePasswordEverySixMonths(userRequest.isChangePasswordEverySixMonths());
        userFoundInDB.setNextDateChangePassword(userRequest.getNextDateChangePassword());
        userFoundInDB.setFirstName(userRequest.getFirstName());
        return userRepository.save(userFoundInDB);
    }

    public Object updateUserByEmail(User updatedUser) {

        User userFoundInDB = getUserByEmail(updatedUser.getEmail());

        return userRepository.save(updateFields(userFoundInDB, updatedUser));
    }

    public User updateFields(User userFoundInDB, User userFromRequest) {
        userFoundInDB.setFirstName(userFromRequest.getFirstName());
        userFoundInDB.setLastName(userFromRequest.getLastName());
        userFoundInDB.setEmail(userFromRequest.getEmail());
        userFoundInDB.setPhoneNumber(userFromRequest.getPhoneNumber());
        userFoundInDB.setPassword(userFromRequest.getPassword());
        userFoundInDB.setSignature(userFromRequest.getSignature());
        userFoundInDB.setBirthDate(userFromRequest.getBirthDate());
        userFoundInDB.setWorkspace(userFromRequest.getWorkspace());
        userFoundInDB.setTwoFactorAuth(userFromRequest.isTwoFactorAuth());
        userFoundInDB.setCertificat(userFromRequest.getCertificat());
        userFoundInDB.setActive(userFromRequest.isActive());
        userFoundInDB.setConfirmed(userFromRequest.isConfirmed());
        userFoundInDB.setRole(userFromRequest.getRole());
        userFoundInDB.setPermissions(userFromRequest.getPermissions());
        userFoundInDB.setPlan(userFromRequest.getPlan());
        return userFoundInDB;
    }

    public boolean isEmailConfirmed(String email) {

        return userRepository.findByEmail(email).get().isConfirmed();
    }

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        // TODO Auto-generated method stub

        User user = getUserByEmail(email);

        if (user == null) {
            throw new ResourceNotFoundExceptionConstimized(TO_LOCALE(EMAIL_NOT_FOUND, LOCALE));
        }

        return user;
    }

    public Optional<User> findById(Long aLong) {
        return userRepository.findById(aLong);
    }

    public List<User> getUsersByIds(List<User> listUsers) {

        List<Long> usersIds = new ArrayList<>();

        for (User user : listUsers) {
            usersIds.add(user.getId());
        }

        List<User> users = userRepository.findAllById(usersIds);

        if (users.isEmpty()) {
            throw new ResourceNotFoundExceptionConstimized(TO_LOCALE(USER_NOT_FOUND, LOCALE));
        }

        return users;
    }

    @Transactional
    public Object deleteUser(Long id) {
        User user = userRepository.findUserById(id);
        //find group_user_ass by user id
        List<GroupUserAssociations> list_group_user_assoc = groupService.findGroupUserByUserId(id);
        //supp
        if (list_group_user_assoc != null) {
            for (GroupUserAssociations grp_ass : list_group_user_assoc) {
                groupService.deleteGroupUserAssosiationById(grp_ass.getId());
            }
        }
        /**
         * 0- get sid id using email
         * 1- get list of acl-entry using sid
         * 2- delete acl-object-identity using object-id for (list-entry)
         * 3- delete list-entry
         * 4- delete sid
         */
        //0
        Integer acl_sid_id = this.aclService.get_acl_sid_by_sid(user.getEmail());
        //1
        List<Integer> list_obj_identity_id = this.aclService.get_acl_object_identity_in_acl_entry_using_sid(acl_sid_id);
        //2
        if (list_obj_identity_id != null) {
            for (Integer obj_identity_id : list_obj_identity_id) {
                this.aclService.delete_obj_identity(obj_identity_id);
            }

            //3
            this.aclService.delete_acl_entry(acl_sid_id);
        }
        //4
        this.aclService.delete_acl_sid(user.getEmail());
        userRepository.deleteById(id);
        return "deleted successfully";
    }

//	public UserDTO convertToDto(User user) {
//
//		UserDTO userDTO = modelMapper.map(user, UserDTO.class);
//
//		return userDTO;
//	}

    public User updatePasswordByEmail(User userRequest) {

        // get the user from database based on the email
        User userFoundInDB = getUserByEmail(userRequest.getEmail());

        // encrypt the password
        String encodedPassword = bCryptPasswordEncoder.encode(userRequest.getPassword());

        // update the password on the old entity with the new one
        userFoundInDB.setPassword(encodedPassword);

        // update in database
        User updatedUser = userRepository.save(userFoundInDB);

        // TODO: DTO layer

        return updatedUser;
    }

    public String sendMailToUpdatePassword(User userRequest) throws IOException, TemplateException, MessagingException {

        // check if the email exist in database
        Optional<User> user = userRepository.findByEmail(userRequest.getEmail());

        if (user.isPresent()) {

            User userInDB = user.get();

            String newPassword = generatePassword();

            // encrypt the password
            String encodedPassword = bCryptPasswordEncoder.encode(newPassword);

            // update the password on the old entity with the new one
            userInDB.setPassword(encodedPassword);

            userRepository.save(userInDB);

            System.out.println("\n\n\n\n\n\n\nnewPassword : " + newPassword);

            // send the mail to the email
            MailComponent mail = new MailComponent("New password : " + newPassword,
                    ConstantStaticStrings.MAIL_SUBJECT_NEW_PASSWORD, null, userRequest.getEmail(), null);

            // TODO: send the mail
            emailService.sendUsingTemplate(mail, ConstantFileNames.NEW_PASSWORD_TEMPLATE_MAIL);

            return "email sent successfully";
        }

        throw new UsernameNotFoundException("this email doesnt exist");
    }

    public String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    public List<User> findByCriteria(String user, Long id) {
        Company company = this.companyService.findCompanyById(id);
        String query = "SELECT o FROM User o where 1=1 AND ";
        if (user != null) {
            query += AbstractService.addConstraint("o", "firstName", user, "LIKE", "");
            query += AbstractService.addConstraint("o", "lastName", user, "LIKE", "OR");
            query += AbstractService.addConstraint("o", "email", user, "LIKE", "OR");
            System.out.println(query);
        }
        List<User> userList = (List<User>) entityManager.createQuery(query).getResultList();
        List<User> returnedUserList = new ArrayList<>();
        for (User u : userList
        ) {
            for (UserCompany com : u.getUserCompanyList()
            ) {
                if (com.getCompany().getId().equals(id)) {
                    System.out.println(u.getEmail());
                    returnedUserList.add(u);
                }
            }
        }
        System.out.println(returnedUserList.size());
        return returnedUserList;
    }

    public User findUserById(Long id) {
        return userRepository.findUserById(id);
    }
}
