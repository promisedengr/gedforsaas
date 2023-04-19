package ma.project.GedforSaas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import ma.project.GedforSaas.model.Notification;
import ma.project.GedforSaas.repository.NoteLabelRepository;
import ma.project.GedforSaas.repository.NotificationRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    @Autowired
    NotificationRepository notificationRepository;

    


    public List<Notification> findByCompanyId(Long id, Long parentId) {
        return notificationRepository.findByCompanyIdAndParentId(id, parentId);
    }

    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }

    public  Notification save(Notification notification) {
        Notification notification1 = notificationRepository.save(notification);
        System.out.println(notification1.getId());
        return notification1;
    }

    public List<Notification> findByUserEmailAndParentId(String email, Long parentId) {
        return notificationRepository.findByUserEmailAndParentId(email, parentId);
    }

    public List<Notification> findByUserEmail(String email) {
        return notificationRepository.findByUserEmail(email);
    }

    public List<Notification> findByGroupId(Long id) {
        return notificationRepository.findByGroupId(id);
    }

    public Optional<Notification> findById(Long aLong) {
        return notificationRepository.findById(aLong);
    }

    @Transactional
    public void deleteById(Long aLong) {
        System.out.println(aLong );
        notificationRepository.deleteById(aLong);
    }

    public List<Notification> get_sent_notification(Long id) {
        return notificationRepository.get_sent_notification(id);
    }

    public List<Notification> get_sent_notification_for_user(String user_email) {
        return notificationRepository.get_sent_notification_for_user(user_email);
    }
}
