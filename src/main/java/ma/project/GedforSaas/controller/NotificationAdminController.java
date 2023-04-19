package ma.project.GedforSaas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ma.project.GedforSaas.model.Notification;
import ma.project.GedforSaas.service.NotificationService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("admin/api/v1/notification")
public class NotificationAdminController {
    @Autowired
    private NotificationService notificationService;


    @GetMapping("/company/id/{id}/{parentId}")
    public List<Notification> getNotification(@PathVariable Long id,@PathVariable  Long parentId) {
        return notificationService.findByCompanyId(id, parentId);
    }


    @GetMapping("/company/id/{id}")
    public List<Notification> findByCompanyId(@PathVariable Long id) {
        return notificationService.findByCompanyId(id, null);
    }

    @GetMapping("/sent/company/id/{id}")
    public List<Notification> get_sent_notification(@PathVariable Long id) {
        return notificationService.get_sent_notification(id);
    }


    @GetMapping("/sent/user/email/{email}")
    public List<Notification> get_sent_notification_for_user(@PathVariable String email) {
        System.out.println(email);
        return notificationService.get_sent_notification_for_user(email);
    }

    @GetMapping("/user/email/{email}")
    public List<Notification> findByUserEmail(@PathVariable String email) {
        return notificationService.findByUserEmail(email);
    }

    @GetMapping("/user/email/{email}/{parentId}")
    public List<Notification> getNotificationByParentAnUser(@PathVariable String email,@PathVariable  Long parentId) {
        return notificationService.findByUserEmailAndParentId(email, parentId);
    }

    @GetMapping("/group/id/{id}")
    public List<Notification> findByGroupId(@PathVariable Long id) {
        return notificationService.findByGroupId(id);
    }

    @GetMapping("/")
    public List<Notification> findAll() {
        return notificationService.findAll();
    }

    @PostMapping("/")
    public Notification save(@RequestBody Notification notification) {
        return notificationService.save(notification);
    }

    @GetMapping("/id/{id}")
    public Optional<Notification> findById(@PathVariable Long id) {
        return notificationService.findById(id);
    }

    @DeleteMapping("/id/{id}")
    public void deleteById(@PathVariable Long id) {
        notificationService.deleteById(id);
    }
}
