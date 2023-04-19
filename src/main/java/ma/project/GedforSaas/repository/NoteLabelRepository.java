package ma.project.GedforSaas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.project.GedforSaas.model.NoteLabel;

@Repository
public interface NoteLabelRepository extends JpaRepository<NoteLabel, Long> {
}
