package ma.project.GedforSaas.service;

import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import ma.project.GedforSaas.model.Company;
import ma.project.GedforSaas.model.ConfirmationToken;
import ma.project.GedforSaas.model.MailComponent;
import ma.project.GedforSaas.model.Role;
import ma.project.GedforSaas.model.RoleEnum;
import ma.project.GedforSaas.model.User;
import ma.project.GedforSaas.model.UserCompany;
import ma.project.GedforSaas.model.UserInfo;
import ma.project.GedforSaas.request.UserAndJWT;
import ma.project.GedforSaas.response.ResponsePayLoad;
import ma.project.GedforSaas.configuration.CallConfiguration;
import ma.project.GedforSaas.configuration.ConstantFileNames;
import ma.project.GedforSaas.exception.RequiredParameterException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Arrays;

import static ma.project.GedforSaas.controller.TranslateController.LOCALE;
import static ma.project.GedforSaas.security.filter.RoleConstant.ROLE_ADMIN;
import static ma.project.GedforSaas.utils.i18n.Translator.TO_LOCALE;
import static ma.project.GedforSaas.utils.i18n.TranslatorCode.*;

@AllArgsConstructor
@Service
public class RegistrationService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ResponsePayLoadService responsePayLoadService;

    @Autowired
    private UserInfoService userInfoService;


    @Autowired
    private GmailService gmailService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private CallConfiguration callConfiguration;

    @Autowired
    private UserCompanyService userCompanyService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private AclService aclService;

    public ResponsePayLoad register(User userRequest) throws IOException, TemplateException, MessagingException {

        // check if the email is not null
        if (userRequest.getEmail() == null) {
            throw new RequiredParameterException(TO_LOCALE(ONE_OR_MULTIPLE_PARAMETERS_REQUEIRED, LOCALE));
        }

        // check if the password is not null
        if (userRequest.getPassword() == null) {
            throw new RequiredParameterException(TO_LOCALE(ONE_OR_MULTIPLE_PARAMETERS_REQUEIRED, LOCALE));
        }
        String lastName = userRequest.getLastName();


        // TODO: encrypt the password
        String encodedPassword = bCryptPasswordEncoder.encode(userRequest.getPassword());

        // TODO: set the old password with the encoded one
        userRequest.setPassword(encodedPassword);

        // TODO: get admin profile
        Role role = roleService.findRoleByName(ROLE_ADMIN);

        // add profile to user
        userRequest.setRole(role);
        userRequest.setAuthorities(Arrays.asList(role));
        userRequest.setConfirmed(false);
        // TODO: save the user
        User userCreatedInDB = userService.addNewUser(userRequest);

        // generate new company and its relationship with
        UserCompany userCompany = userCompanyService.generateNewUserCompany(userCreatedInDB);

        // add new confirmation token
        String token = confirmationTokenService.addNewConfirmationToken(userRequest);

        String link = callConfiguration.getConfigurationByName("primary.link.for.reset.password") + userCreatedInDB.getId() + "/"
                + token;

        MailComponent mail = new MailComponent(link, TO_LOCALE(MAIL_SUBJECT_CONFIRM_REGISTRATION, LOCALE), null,
                userRequest.getEmail(), null);

        // TODO: send the mail
        emailSenderService.sendUsingTemplate(mail, ConstantFileNames.CONFIRMATION_TEMPLATE_MAIL);

        return responsePayLoadService.generateNewPayLoad(
                TO_LOCALE(REGISTRATION_MAIL_SENT_SUCCESSFULLY, LOCALE), userRequest);
    }

    public ResponsePayLoad confirmEmail(String token) throws Exception {

        // TODO: search by token
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token);

        // TODO: get the user
        User userFromToken = confirmationToken.getUser();


        /**
         * Insert every new user in ACL_SID table
         *
         * @Param principal =1 is sid is a user
         * @Param principal =0 is sid is a role
         */
        this.aclService.insert_acl_sid("1", userFromToken.getEmail());


        // TODO: authenticate automatically
        String jwt = authenticationService.automaticAuthenticate(userFromToken);

        // TODO: change the isConfirmed
        userFromToken.setConfirmed(true);

        // TODO: save the changes in DB
        userService.updateUserById(userFromToken.getId(), userFromToken);

        /**
         * create customer in stripe
         * @Param: userFromToken
         * @return: User with customerID
         */
        User myUser = this.paymentService.createCustomer(userFromToken);
        UserInfo userInfo = userInfoService.getUserInfoFromUser(myUser);

        return responsePayLoadService.generateNewPayLoad(TO_LOCALE(REGISTRATION_MAIL_SENT_SUCCESSFULLY, LOCALE),
                new UserAndJWT(jwt, userInfo));
    }


    public String registerByInvitation(String email, Long companyId) throws IOException, TemplateException, MessagingException {
        User userRequest = new User();
        userRequest.setEmail(email);
        userRequest.setConfirmed(false);

        Company company = companyService.findCompanyById(companyId);
        UserCompany userCompany = new UserCompany();


        // check if the email is not null
        if (email == null) {
            throw new RequiredParameterException(TO_LOCALE(ONE_OR_MULTIPLE_PARAMETERS_REQUEIRED, LOCALE));
        }

        // get user profile
        Role role = roleService.findRoleByName(RoleEnum.ROLE_USER.name());

        // add profile to user
        userRequest.setRole(role);
        userRequest.setAuthorities(Arrays.asList(role));

        // TODO: save the user
        User userInDB = userService.addNewUser(userRequest);

        //generate new company and its relationship with


        userCompany.setUser(userInDB);
        userCompany.setCompany(company);
        userCompany.setRole(role);

        userCompanyService.save(userCompany);
        // add new confirmation token
        String token = confirmationTokenService.addNewConfirmationToken(userRequest);

        //
        String link = callConfiguration.getConfigurationByName("primary.link.for.reset.password") + userRequest.getId() + "/"
                + token;

        MailComponent mail = new MailComponent(link, TO_LOCALE(MAIL_SUBJECT_CONFIRM_REGISTRATION, LOCALE), null,
                userRequest.getEmail(), null);

        // TODO: send the mail
        emailSenderService.sendUsingTemplate(mail, ConstantFileNames.CONFIRMATION_TEMPLATE_MAIL);
        return token;
    }

    public String confirmInvitation(String token) {

        // TODO: search by token throws an exception if doesnt exist
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token);

        // TODO: get user from token
        User userFromToken = confirmationToken.getUser();

        /**
         * Insert every new user in ACL_SID table
         *
         * @Param principal =1 is sid is a user
         * @Param principal =0 is sid is a role
         */
        this.aclService.insert_acl_sid("1", userFromToken.getEmail());

        // TODO: check if he did confirm it already
        if (userFromToken.isConfirmed()) {
            throw new RequiredParameterException(TO_LOCALE(EMAIL_ALREADY_EXISTE_AND_CONFIRMED, LOCALE));
        }

        // TODO: set the status confirmed of the user to true
        userFromToken.setConfirmed(true);

        // TODO: save the changes in DB
        userService.updateUserById(userFromToken.getId(), userFromToken);

        return "the request is handled successfully";
    }


}
