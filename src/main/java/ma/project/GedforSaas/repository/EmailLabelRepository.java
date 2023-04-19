package ma.project.GedforSaas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.project.GedforSaas.model.EmailLabel;

@Repository
public interface EmailLabelRepository extends JpaRepository<EmailLabel,Long> {
}
