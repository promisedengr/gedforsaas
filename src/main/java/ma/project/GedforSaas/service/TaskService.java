package ma.project.GedforSaas.service;


import ma.project.GedforSaas.abstractSupperClasses.AbstractService;
import ma.project.GedforSaas.model.Company;
import ma.project.GedforSaas.model.Resource;
import ma.project.GedforSaas.model.Task;
import ma.project.GedforSaas.repository.TaskRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ma.project.GedforSaas.controller.TranslateController.LOCALE;
import static ma.project.GedforSaas.utils.i18n.Translator.TO_LOCALE;
import static ma.project.GedforSaas.utils.i18n.TranslatorCode.COMPANY_NOT_FOUND;

@Service
@EnableScheduling
public class TaskService {
    private List<Task> taskList = new ArrayList<>(); // All database not reminded tasks, check every task to send notification to user




    public Task findByName(String name) {
        return taskRepository.findByName(name);
    }



    public List<Task> findByCompanyId(Long id) {
        return taskRepository.findByCompanyId(id);
    }

    public List<Task> findByResourceId(Long resourceId) {
        return taskRepository.findByResourceId(resourceId);
    }

    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> saveAll(List<Task> tasks) {
        return taskRepository.saveAll(tasks);
    }

    public Task save(Task task) throws Exception {
        System.out.println(task.getResource().getId());
        Company company = companyService.findCompanyById(task.getCompany().getId());
        if (task.getResource() != null) {
            if (task.getResource().getId() != null) {
                Resource resource = this.resourceService.findResourceById(task.getResource().getId());
                task.setResource(resource);
            } else {
                task.setResource(null);
            }
        }

        if (company == null) {
            throw new Exception(TO_LOCALE(COMPANY_NOT_FOUND,LOCALE));
        } else {
            task.setCompany(company);
        }
        Task t = taskRepository.save(task);
        this.taskList = this.findAllTasks(); // update taskList to add new task
        return t;
    }

    public Optional<Task> findById(Long aLong) {
        return taskRepository.findById(aLong);
    }

    @Scheduled(cron = "#{scheduleCalculator.getCron()}")
    public void rappel() throws MessagingException, IOException {
        LocalDateTime date = LocalDateTime.now();
        if (this.taskList.size() == 0) {
            this.taskList = this.findAllTasks();
        }
        for (Task task : this.taskList) { // check every task if time to send notification
            double duree = task.getRappel() * 3600000; // convert hours to milliseconds
            if (date.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli() <= task.getDateCreated().atOffset(ZoneOffset.UTC).toInstant().toEpochMilli() && task.getDateCreated().atOffset(ZoneOffset.UTC).toInstant().toEpochMilli() - duree < date.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli()) {
                System.out.println("condition is true"); //TODO: send notification to user
                assert task.getResource() != null;
                this.gmailService.sendEmail(
                        "Notification de type : Tache\n" +
                                "nom de tache : " + task.getName() + "\n" +
                                "Date d'échéance: " + task.getDueDate() + "\n" +
                                "Priorité : " + task.getPriority() + "\n" +
                                "Description : " + task.getDescription() + ".\n" ,
                        "Bonjour " + task.getResource().getLastName() + " " + task.getResource().getFirstName()

                        , task.getResource().getPersonalEmail()
                        );
            }
        }
    }

    public List<Task> findByCriteria(String task, Long id) {
        String query = AbstractService.init(id, "Task"); // here we add table name be careful to make first letter Uppercase
        query += AbstractService.addConstraint("o", "resource.firstName", task, "LIKE", "");
        query += AbstractService.addConstraint("o", "assignedUser.firstName", task, "LIKE", "OR");
        query += AbstractService.addConstraint("o", "assignedUser.lastName", task, "LIKE", "OR");
        query += AbstractService.addConstraint("o", "assignedUser.email", task, "LIKE", "OR");
        query += AbstractService.addConstraint("o", "resource.lastName", task, "LIKE", "OR");
        query += AbstractService.addConstraint("o", "resource.personalEmail", task, "LIKE", "OR");
        query += AbstractService.addConstraint("o", "description", task, "LIKE", "OR");
        query += AbstractService.addConstraint("o", "status", task, "LIKE", "OR");
        query += AbstractService.addConstraint("o", "priority", task, "LIKE", "OR");
        query += AbstractService.addConstraint("o", "name", task, "LIKE", "OR");
        System.out.println(query);
        return entityManager.createQuery(query).getResultList();
    }

    public List<Task> findBetweenStartAndEndDate(String startDateString, String finishDateString, Long id) {
        String query = AbstractService.init(id, "Task");
        query += AbstractService.addConstraintMinMaxDate("o", "dueDate", startDateString, finishDateString);
        System.out.println(query);
        return entityManager.createQuery(query).getResultList();
    }

    @Transactional
    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private GmailService gmailService;

}
