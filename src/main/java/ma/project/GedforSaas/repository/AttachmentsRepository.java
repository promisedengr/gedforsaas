package ma.project.GedforSaas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.project.GedforSaas.model.Attachments;

@Repository
public interface AttachmentsRepository extends JpaRepository<Attachments,Long> {
}
