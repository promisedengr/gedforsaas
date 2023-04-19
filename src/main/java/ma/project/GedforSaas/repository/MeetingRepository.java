package ma.project.GedforSaas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.project.GedforSaas.model.Meeting;

import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting,Long> {

    List<Meeting> findByResourceId(Long resourceId);
    List<Meeting> findByCompanyId(Long id);
    Meeting findMeetingById(Long id);
}
