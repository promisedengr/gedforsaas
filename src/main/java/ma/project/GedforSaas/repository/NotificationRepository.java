package ma.project.GedforSaas.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ma.project.GedforSaas.model.Notification;

import java.util.List;

@Repository
public interface NotificationRepository  extends JpaRepository<Notification, Long> {
    List<Notification> findByCompanyIdAndParentId(Long id, Long parentId);
    List<Notification> findByUserEmail(String email);
    List<Notification> findByUserEmailAndParentId(String email, Long parentId);
    List<Notification> findByGroupId(Long id);
    
    @Query(
            value = "SELECT * FROM Notification   WHERE company_company_id =:id And parent_id is not NULL ",
            nativeQuery = true)
    List<Notification> get_sent_notification(@Param("id") Long id);


    @Query(
            value = "SELECT * FROM Notification   WHERE user_email =:user_email And parent_id is not NULL ",
            nativeQuery = true)
    List<Notification> get_sent_notification_for_user(@Param("user_email") String user_email);

}
