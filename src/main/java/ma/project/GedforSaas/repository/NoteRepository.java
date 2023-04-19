package ma.project.GedforSaas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.project.GedforSaas.model.Note;
import ma.project.GedforSaas.model.NoteLabel;

import java.util.Collection;
import java.util.List;


@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByResourceId(Long id);
    List<Note> findByCompanyId(Long id);
    List<Note> findByLabelsIn(Collection<List<NoteLabel>> labels);
    List<Note> findNotesByLabelsIn(Collection<List<NoteLabel>> labels);
}
