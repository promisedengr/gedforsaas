package ma.project.GedforSaas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ma.project.GedforSaas.model.EmailFilter;

@Repository
public interface EmailFilterRepository extends JpaRepository<EmailFilter,Long> {
}
