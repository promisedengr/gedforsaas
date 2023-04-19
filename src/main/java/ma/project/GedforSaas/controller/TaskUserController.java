package ma.project.GedforSaas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.web.bind.annotation.*;

import ma.project.GedforSaas.model.Task;
import ma.project.GedforSaas.service.TaskService;
import ma.project.GedforSaas.utils.ScheduleCalculator;

import java.util.List;

@CrossOrigin("http://localhost:4200")
@RequestMapping("user/api/task")
@RestController
public class TaskUserController {



    @GetMapping("/resource/id/{resourceId}")
    public List<Task> findByResourceId(@PathVariable Long resourceId) {
        return taskService.findByResourceId(resourceId);
    }

    @GetMapping("/company/id/{id}")
    public List<Task> findByCompanyId(@PathVariable Long id) {
        return taskService.findByCompanyId(id);
    }

    @GetMapping("/name/{name}")
    public Task findByName(@PathVariable String name) {
        return taskService.findByName(name);
    }


    @GetMapping("/")
    public List<Task> findAllTasks() {
        return taskService.findAllTasks();
    }

    @PostMapping("/")
    public Task save(@RequestBody Task task) throws Exception {
        return taskService.save(task);
    }

    @GetMapping("/update/cron/{repeatTime}")
    public void updateCron(@PathVariable int repeatTime) {
        this.stopSchedule(); // required to stop scheduling before update cron
        scheduleCalculator.updateCron(repeatTime);
        this.startSchedule(); // start scheduling after cron updated
    }

    @GetMapping("/findByCriteria/{companyId}/{task}")
    public List<Task> findByCriteria(@PathVariable String task,@PathVariable Long companyId) {
        return taskService.findByCriteria(task, companyId);
    }

    @GetMapping("id/{companyId}/startDate/{startDate}/finishDate/{finishDate}")
    public List<Task> findBetweenStartAndEndDate(@PathVariable String startDate,@PathVariable Long companyId, @PathVariable String finishDate) {
        return taskService.findBetweenStartAndEndDate(startDate, finishDate,companyId);
    }



    @DeleteMapping("/id/{id}")
    public void deleteById(@PathVariable Long id) {
        System.out.println(id);
        taskService.deleteById(id);
    }

    private static final String SCHEDULED_TASKS = "scheduledTasks";


    public void stopSchedule() { // used to stop scheduling rappel in taskService
        postProcessor.postProcessBeforeDestruction(taskService, SCHEDULED_TASKS);
    }

    // used to restart scheduling rappel in taskService without stopping server
    public void startSchedule() {
        postProcessor.postProcessAfterInitialization(taskService, SCHEDULED_TASKS);
    }

    @Autowired
    private ScheduledAnnotationBeanPostProcessor postProcessor;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ScheduleCalculator scheduleCalculator;
}
