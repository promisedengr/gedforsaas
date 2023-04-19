package ma.project.GedforSaas.service;

import freemarker.template.TemplateException;
import ma.project.GedforSaas.model.Company;
import ma.project.GedforSaas.model.Document;
import ma.project.GedforSaas.model.MailComponent;
import ma.project.GedforSaas.model.Note;
import ma.project.GedforSaas.model.Resource;
import ma.project.GedforSaas.model.Task;
import ma.project.GedforSaas.repository.ResourceRepository;
import ma.project.GedforSaas.exception.ResourceAlreadyExistCustomized;
import ma.project.GedforSaas.exception.ResourceNotFoundExceptionConstimized;
import ma.project.GedforSaas.abstractSupperClasses.AbstractService;
import ma.project.GedforSaas.configuration.ConstantFileNames;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static ma.project.GedforSaas.controller.TranslateController.LOCALE;
import static ma.project.GedforSaas.utils.i18n.Translator.TO_LOCALE;
import static ma.project.GedforSaas.utils.i18n.TranslatorCode.*;

@Service
@Transactional
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private CompanyService companyService;

    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private NoteService noteService;


    public List<Resource> findByCompanyId(Long id) {
        return resourceRepository.findByCompanyIdAndTrash(id, false);
    }

    public List<Resource> findByTrashResources(Long id) {
        return resourceRepository.findByCompanyIdAndTrash(id, true);
    }

    // create new resource
    public Resource addResource (Resource resource) throws Exception {
        System.out.println(resource.getCompany().getId());
        Company company = companyService.findCompanyById(resource.getCompany().getId());
        if (company == null) {
            throw new Exception(TO_LOCALE(COMPANY_NOT_FOUND,LOCALE));
        } else {
            resource.setCompany(company);
        }
        // check if the email exist already
        checkIfPersonalEmailExist(resource);

        // if it doesnt exist save the new resource
        return resourceRepository.save(resource);
    }

    public List<Resource> findAllResources() {
        return resourceRepository.findAll();
    }

    public Resource updateResource(Long id, Resource resource) {

        // get the resource from data
        Resource resourceInDB = findResourceById(id);

        // if the in the database isnt the same as the one in the request body
        if (!resource.getPersonalEmail().equals(resourceInDB.getPersonalEmail())) {

            // check if the email exist already on other entities
            // if yes the method is gonna throw an exception
            checkIfPersonalEmailExist(resource);
        }

        // if the email in database is the same as the one in the request continue the
        // update
        resourceInDB.setAdress(resource.getAdress());
        resourceInDB.setBirthDate(resource.getBirthDate());
        resourceInDB.setCampagnyName(resource.getCampagnyName());
        resourceInDB.setCity(resource.getCity());
        resourceInDB.setCompany(resource.getCompany());
        resourceInDB.setCountry(resource.getCountry());
        resourceInDB.setFirstName(resource.getFirstName());
        resourceInDB.setLastName(resource.getLastName());
        resourceInDB.setPersonalEmail(resource.getPersonalEmail());
        resourceInDB.setPhoneNumber(resource.getPhoneNumber());
        resourceInDB.setProfessionalEmail(resource.getProfessionalEmail());

        // save it in database
        return resourceRepository.save(resource);
    }

    public Resource findResourceById(Long id) {
        return resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExceptionConstimized(TO_LOCALE(RESOURCE_NOT_FOUND, LOCALE)));
    }


    public void deleteResource(Long id) {
    	List<Note> notes =noteService.findByResourceId(id);
    	if(notes!=null) {
			for (Note note : notes) {
				noteService.deleteNoteById(note.getId());
			}
    	}
    	List<Task> tasks = taskService.findByResourceId(id);
    	if(tasks!=null) {
    		for(Task task : tasks) {
    			taskService.deleteById(task.getId());
    		}
    	}
        resourceRepository.deleteById(id);
    }
    // add and remove resource from trash
    public Resource addResourceOnTrash(Resource resource) {
        return resourceRepository.save(resource);
    }

    public Resource getResourceByPersonalEmail(Resource resource) {

        return resourceRepository.findByPersonalEmail(resource.getPersonalEmail())
                .orElseThrow(() -> new ResourceNotFoundExceptionConstimized(TO_LOCALE(RESOURCE_NOT_FOUND, LOCALE)));
    }

    public Resource getResourceById(Long id) {
        Optional<Resource> resource = resourceRepository.findById(id);

        if (!resource.isPresent()) {
            throw new ResourceNotFoundExceptionConstimized(TO_LOCALE(ID_NOT_FOUND, LOCALE));
        }

        return resource.get();
    }

    // if its exist
    public boolean checkIfPersonalEmailExist(Resource resource) {

        Optional<Resource> resourceInDB = resourceRepository.findByPersonalEmail(resource.getPersonalEmail());

        if (resourceInDB.isPresent()) {
            throw new ResourceAlreadyExistCustomized(TO_LOCALE(RESOURCE_ALREADY_EXIST, LOCALE));
        }

        return false;
    }

    public Resource changeState(Long id, boolean active) {

        // get resource by id
        Resource resource = findResourceById(id);

        // change state of active to the one received
        resource.setActive(active);

        return resourceRepository.save(resource);
    }

    public Object sendMail(Resource resource) throws IOException, TemplateException, MessagingException {

        // prepare the link to pass into the mail

        // prepare the subject to pass into the mail

        // prepare the content

        // prepare to whom

        // prepare from

        MailComponent mailComponent = new MailComponent("tata", TO_LOCALE(MAIL_SUBJECT_MAIL_TO_RESOURCE, LOCALE),
                null, resource.getPersonalEmail(), null);

        emailSenderService.sendUsingTemplate(mailComponent, ConstantFileNames.CONFIRMATION_TEMPLATE_MAIL);

        return null;
    }


    public List<Resource> findByCriteria(String text, Long id) {
        String query = AbstractService.init(id, "Resource"); // here we add table name be careful to make first letter Uppercase
        if (text != null) {
            query += AbstractService.addConstraint("o", "firstName", text, "LIKE", "");
            query += AbstractService.addConstraint("o", "lastName", text, "LIKE", "OR");
            query += AbstractService.addConstraint("o", "personalEmail", text, "LIKE", "OR");
            query += AbstractService.addConstraint("o", "professionalEmail", text, "LIKE", "OR");
            query += AbstractService.addConstraint("o", "adress", text, "LIKE", "OR");
            query += AbstractService.addConstraint("o", "city", text, "LIKE", "OR");
            query += AbstractService.addConstraint("o", "country", text, "LIKE", "OR");
        }
        System.out.println(query);
        return entityManager.createQuery(query).getResultList();
    }


}
