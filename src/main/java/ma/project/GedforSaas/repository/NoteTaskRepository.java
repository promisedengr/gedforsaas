package ma.project.GedforSaas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.project.GedforSaas.model.NoteTask;

@Repository
public interface NoteTaskRepository extends JpaRepository<NoteTask, Long> {
}
