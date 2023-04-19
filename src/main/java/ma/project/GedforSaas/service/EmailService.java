package ma.project.GedforSaas.service;

import ma.project.GedforSaas.model.Attachments;
import ma.project.GedforSaas.model.Company;
import ma.project.GedforSaas.model.Email;
import ma.project.GedforSaas.model.EmailCategory;
import ma.project.GedforSaas.model.EmailFilter;
import ma.project.GedforSaas.model.EmailLabel;
import ma.project.GedforSaas.model.MailComponent;
import ma.project.GedforSaas.model.Resource;
import ma.project.GedforSaas.model.User;
import ma.project.GedforSaas.repository.AttachmentsRepository;
import ma.project.GedforSaas.repository.EmailCategoryRepository;
import ma.project.GedforSaas.repository.EmailFilterRepository;
import ma.project.GedforSaas.repository.EmailLabelRepository;
import ma.project.GedforSaas.repository.EmailRepository;
import ma.project.GedforSaas.abstractSupperClasses.AbstractService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static ma.project.GedforSaas.controller.TranslateController.LOCALE;
import static ma.project.GedforSaas.utils.i18n.Translator.TO_LOCALE;
import static ma.project.GedforSaas.utils.i18n.TranslatorCode.COMPANY_NOT_FOUND;

@Service
public class EmailService {


    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private EmailSenderService gmailService;

    @Autowired
    private UserService userService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private EmailLabelRepository emailLabelRepository;
    @Autowired
    private EmailFilterRepository emailFilterRepository;
    @Autowired
    private EmailCategoryRepository emailCategoryRepository;
    @Autowired
    private AttachmentsRepository attachmentsRepository;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private EntityManager entityManager;


    public Email save(Email email) throws Exception {
        Company company = companyService.findCompanyById(email.getCompany().getId());
        if (email.getToUser() != null) {
            Resource resource = resourceService.getResourceById(email.getToUser().getId());
            if (resource == null) {
                throw new Exception("Contact " + email.getToUser().getLastName() + "not found");
            } else {
                email.setToUser(resource);
            }
        }

        if (company == null) {
            throw new Exception(TO_LOCALE(COMPANY_NOT_FOUND, LOCALE));
        } else {
            email.setCompany(company);
        }
        User fromUser = this.userService.loadUserByUsername(email.getFromUser().getEmail());
        email.setFromUser(fromUser);
        MailComponent mailComponent = new MailComponent();
        if (email.getToUser() == null) {
            mailComponent.setTo("ahmed.dokumentive@gmail.com");
        } else {
            mailComponent.setTo(email.getToUser().getPersonalEmail());
        }
        mailComponent.setSubject(email.getSubject());
        mailComponent.setContent(email.getContent() + "\n" + email.getSubject() + '\n' + email.getType() + "\n" +
                email.getDescription());
        this.gmailService.send(mailComponent);
        return emailRepository.save(email);
    }


    public Optional<Email> findById(Long id) {
        return emailRepository.findById(id);
    }

    public void update(Email email) {
        emailRepository.save(email);
    }


    public List<Email> findAll() {
        return emailRepository.findAll();
    }


    @Transactional
    public void deleteById(Long id) {
        emailRepository.deleteById(id);
    }

	/*

	EmailLabel
	 */

    public List<EmailLabel> findAllEmaillabel() {
        return emailLabelRepository.findAll();
    }

    public Optional<EmailLabel> findEmaillabelById(Long id) {
        return emailLabelRepository.findById(id);
    }

    @Transactional
    public void deleteEmaillabelById(Long id) {
        emailLabelRepository.deleteById(id);
    }


    public EmailLabel saveEmaillabel(EmailLabel entity) {
        return emailLabelRepository.save(entity);
    }


    /*

    EmailFilter
     */
    public List<EmailFilter> findAllEmailfilter() {
        return emailFilterRepository.findAll();
    }

    public Optional<EmailFilter> findEmailfilterById(Long id) {
        return emailFilterRepository.findById(id);
    }

    @Transactional
    public void deleteEmailfilterById(Long id) {
        emailFilterRepository.deleteById(id);
    }


    public EmailFilter saveEmailfilter(EmailFilter entity) {
        return emailFilterRepository.save(entity);
    }


	/*

	EmailCategory
	 */

    public List<EmailCategory> findAllEmailcategory() {
        return emailCategoryRepository.findAll();
    }

    public Optional<EmailCategory> findEmailcategoryById(Long id) {
        return emailCategoryRepository.findById(id);
    }

    @Transactional
    public void deleteEmailcategoryById(Long id) {
        emailCategoryRepository.deleteById(id);
    }


    public EmailCategory saveEmailcategory(EmailCategory entity) {
        return emailCategoryRepository.save(entity);
    }

	/*

	EmailAttachments
	 */

    public List<Attachments> findAllattachments() {
        return attachmentsRepository.findAll();
    }

    public Optional<Attachments> findattachmentsById(Long id) {
        return attachmentsRepository.findById(id);
    }

    @Transactional
    public void deleteattachmentsById(Long id) {
        attachmentsRepository.deleteById(id);
    }

    public List<Attachments> saveAttachment(Attachments entity) {
        attachmentsRepository.save(entity);
        return this.findAllattachments();
    }

    public List<Email> findByToUserId(Long id) {
        return emailRepository.findByToUserId(id);
    }

    public List<Email> findByFromUserId(Long id) {
        return emailRepository.findByFromUserId(id);
    }

    public List<Email> findCriteriaByFolderAndFromUserIdOrToUserId(String folder, Long id, Long companyId) {
        String query = AbstractService.init(companyId, "Email"); // here we add table name be careful to make first letter Uppercase
        if (folder.equals("sent")) {
            query += AbstractService.addConstraint("o", "fromUser.id", id, "=", "");
        } else if (folder.equals("inbox")) {
            query += AbstractService.addConstraint("o", "folder", folder, "LIKE", "");
            query += AbstractService.addConstraint("o", "toUser.id", id, "=", "AND");
        } else {
            query += AbstractService.addConstraint("o", "folder", folder, "LIKE", "");
            query += AbstractService.addConstraint("o", "fromUser.id", id, "=", "AND");
            query += AbstractService.addConstraint("o", "toUser.id", id, "=", "OR");
        }
        System.out.println(query);
        return entityManager.createQuery(query).getResultList();
    }

    public List<Email> findCriteriaByFolderAndToUserId(Long id, Long companyId) {
        String query = AbstractService.init(companyId, "Email"); // here we add table name be careful to make first letter Uppercase
        query += AbstractService.addConstraint("o", "folder", "inbox", "LIKE", "");
        query += AbstractService.addConstraint("o", "toUser.id", id, "=", "AND");
        System.out.println(query);
        return entityManager.createQuery(query).getResultList();
    }


    public List<Email> findCriteriaByUserId(Long id, Long companyId) {
        String query = AbstractService.init(companyId, "Email");
        query += AbstractService.addConstraint("o", "fromUser.id", id, "=", "");
        query += AbstractService.addConstraint("o", "toUser.id", id, "=", "OR");
        System.out.println(query);
        return entityManager.createQuery(query).getResultList();
    }

    public List<Email> findCriteriaByUserIdAndDateBetween(Long id, Long companyId) {
        LocalDateTime actualDate = LocalDateTime.now();
        LocalDateTime yesterdayDate = LocalDateTime.of(actualDate.getYear(), actualDate.getMonth(), actualDate.getDayOfMonth() - 1,
                actualDate.getHour(), actualDate.getMinute(), actualDate.getSecond());

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String actualDateTimeString = actualDate.format(dateTimeFormatter);
        String yesterdayDateTimeString = yesterdayDate.format(dateTimeFormatter);

        String query = AbstractService.init(companyId, "Email");
        query += AbstractService.addConstraint("o", "fromUser.id", id, "=", "");
        query += AbstractService.addConstraintMinMaxDate("o", "date", yesterdayDateTimeString, actualDateTimeString);
        System.out.println(query);
        return entityManager.createQuery(query).getResultList();
    }

    public List<Email> findCriteriaByUserIdAndFilter(String filter, Long id, Long companyId) {
        String query = AbstractService.init(companyId, "Email");
        query += AbstractService.addConstraint("o", "fromUser.id", id, "=", " (");
        query += AbstractService.addConstraint("o", "toUser.id", id, "=", "OR");
        if (filter.equals("important")) {
            query += AbstractService.addConstraint("o", "important", true, "=", ") AND");
        } else {
            query += AbstractService.addConstraint("o", "starred", true, "=", ") AND");
        }
        System.out.println(query);
        System.out.println(entityManager.createQuery(query).getResultList().size());
        return entityManager.createQuery(query).getResultList();
    }

    public List<Email> findByCompanyId(Long id) {
        return emailRepository.findByCompanyId(id);
    }
}
