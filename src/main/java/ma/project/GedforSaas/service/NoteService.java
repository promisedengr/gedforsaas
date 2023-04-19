package ma.project.GedforSaas.service;

import ma.project.GedforSaas.model.Company;
import ma.project.GedforSaas.model.Note;
import ma.project.GedforSaas.model.NoteLabel;
import ma.project.GedforSaas.model.NoteTask;
import ma.project.GedforSaas.model.Resource;
import ma.project.GedforSaas.repository.NoteLabelRepository;
import ma.project.GedforSaas.repository.NoteRepository;
import ma.project.GedforSaas.repository.NoteTaskRepository;
import ma.project.GedforSaas.abstractSupperClasses.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static ma.project.GedforSaas.controller.TranslateController.LOCALE;
import static ma.project.GedforSaas.utils.i18n.Translator.TO_LOCALE;
import static ma.project.GedforSaas.utils.i18n.TranslatorCode.COMPANY_NOT_FOUND;
import static ma.project.GedforSaas.utils.i18n.TranslatorCode.RESOURCE_NOT_FOUND;

@Service
public class NoteService {


    public Optional<Note> findNoteById(Long id) {
        return noteRepository.findById(id);

    }

    public List<Note> findByLabelsIn(Collection<List<NoteLabel>> labels) {
        return noteRepository.findNotesByLabelsIn(labels);
    }

    public List<Note> findByCompanyId(Long id) {
        return noteRepository.findByCompanyId(id);
    }

    public Note saveNote(Note note) throws Exception {
        Company company = companyService.findCompanyById(note.getCompany().getId());
        if (note.getResource() != null) {
            System.out.println(note.getResource().getId());
            Resource resource = resourceService.getResourceById(note.getResource().getId());
            if (resource == null) {
                throw new Exception(TO_LOCALE(RESOURCE_NOT_FOUND, LOCALE));
            } else {
                note.setResource(resource);
            }
        }
        if (company == null) {
            throw new Exception(TO_LOCALE(COMPANY_NOT_FOUND,LOCALE));
        } else {
            note.setCompany(company);
        }
        Note uploadedNote = noteRepository.save(note);
        List<NoteTask> noteTasks = new ArrayList<>();
        if (note.getTasks() != null) {
            for (NoteTask n : note.getTasks()
            ) {
                n.setNote(uploadedNote);
                System.out.println(n.getContent());

                noteTasks.add(this.saveNoteTask(n));
            }
            uploadedNote.setTasks(noteTasks);
        }
        return uploadedNote;
    }

    public List<Note> findByResourceId(Long id) {
        return noteRepository.findByResourceId(id);
    }

    public void updateNote(Note note) {
        noteRepository.save(note);
    }


    public List<Note> findAllNotes() {
        return noteRepository.findAll();
    }


    @Transactional
    public boolean deleteNoteById(Long id) {
        noteRepository.deleteById(id);
        return true;
    }

    /*
     *
     ****
     * 			NoteLabel
     */

    public List<NoteLabel> findAllNoteLabel() {
        return noteLabelRepository.findAll();
    }

    public List<NoteLabel> saveNoteLabel(NoteLabel entity) {
        noteLabelRepository.save(entity);
        return this.findAllNoteLabel();
    }

    public Optional<NoteLabel> findNoteLabelById(Long aLong) {
        return noteLabelRepository.findById(aLong);
    }

    @Transactional
    public void deleteNoteLabelById(Long aLong) {
        noteLabelRepository.deleteById(aLong);
    }

    /*
     *
     ****
     * 			NoteTask
     */

    public List<NoteTask> findAllNoteTask() {
        return noteTaskRepository.findAll();
    }

    public NoteTask saveNoteTask(NoteTask entity) throws Exception {
        Optional<Note> note = this.findNoteById(entity.getNote().getId());
        if (!note.isPresent()) {
            throw new Exception("Note not found !");
        }
        entity.setNote(note.get());
        return noteTaskRepository.save(entity);
    }

    public Optional<NoteTask> findNoteTaskById(Long aLong) {
        return noteTaskRepository.findById(aLong);
    }

    @Transactional
    public void deleteNoteTaskById(Long aLong) {
        noteTaskRepository.deleteById(aLong);
    }


    public List<Note> findByCriteria(String text, Long id) {
        String query = AbstractService.init(id, "Note"); // here we add table name be careful to make first letter Uppercase
        System.out.println(text);
        if (text != null) {
            query += AbstractService.addConstraint("o", "title", text, "LIKE", "");
            query += AbstractService.addConstraint("o", "content", text, "LIKE", "OR");
        }
        return entityManager.createQuery(query).getResultList();
    }

    public void saveAllNoteTask(List<NoteTask> noteTasks, Note note) throws Exception {
        for (NoteTask noteTask : noteTasks
        ) {
            noteTask.setNote(note);
            this.saveNoteTask(noteTask);
        }

    }


    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private NoteLabelRepository noteLabelRepository;
    @Autowired
    private NoteTaskRepository noteTaskRepository;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private EntityManager entityManager;


}
