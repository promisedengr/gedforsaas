package ma.project.GedforSaas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.project.GedforSaas.model.Email;

import java.util.Date;
import java.util.List;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

    List<Email> findByToUserId(Long id);
    List<Email> findByToUserIdAndDate(Long id, Date date);
    List<Email> findByFromUserId(Long id);
    List<Email> findByCompanyId(Long id);
}
