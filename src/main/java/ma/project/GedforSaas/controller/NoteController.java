package ma.project.GedforSaas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.web.bind.annotation.*;

import ma.project.GedforSaas.model.Note;
import ma.project.GedforSaas.model.NoteLabel;
import ma.project.GedforSaas.model.NoteTask;
import ma.project.GedforSaas.service.NoteService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/note")
public class NoteController {

    @Autowired
    private NoteService noteService;


    @GetMapping("/findBy/labels/{id}")
    public List<Note> findByLabelsIn(@PathVariable Long id ) {
        Collection<List<NoteLabel>> labels = new ArrayList<>();
        List<NoteLabel> noteLabels = new ArrayList<>();
        Optional<NoteLabel> noteLabel = this.noteService.findNoteLabelById(id);
        if (noteLabel.isPresent()){
            System.out.println(id);
            noteLabels.add(noteLabel.get());
            labels.add(noteLabels);
            return noteService.findByLabelsIn(labels);
        }
        return noteService.findAllNotes();
    }

    @GetMapping("/company/id/{id}")
    public List<Note> findByCompanyId(@PathVariable Long id) {
        return noteService.findByCompanyId(id);
    }

    @PostMapping("/")
    public Note saveNote(@RequestBody Note note) throws Exception {
        return noteService.saveNote(note);
    }
    @PostMapping("/{id}/update")
    public Note update(@RequestBody Note note, @PathVariable Long id)
            throws Exception {
        Optional<Note> noteDB = this.noteService.findNoteById(id);
        if (noteDB.isPresent()){
            noteDB.get().setTasks(note.getTasks());
            noteDB.get().setContent(note.getContent());
            noteDB.get().setResource(note.getResource());
            noteDB.get().setFavorite(note.isFavorite());
            noteDB.get().setImage(note.getImage());
            noteDB.get().setLabels(note.getLabels());
            noteDB.get().setTitle(note.getTitle());
            this.noteService.saveAllNoteTask(note.getTasks(), noteDB.get());
            return noteService.saveNote(noteDB.get());
        } else throw new NotFoundException("Note not found .");

    }

    @GetMapping("resource/id/{id}")
    public List<Note> findByResourceId(@PathVariable Long id) {
        return noteService.findByResourceId(id);
    }

    @GetMapping("/")
    public List<Note> findAllNotes() {
        return noteService.findAllNotes();
    }


    @DeleteMapping("/id/{id}")
    public boolean deleteNoteById(@PathVariable Long id) {
        return  noteService.deleteNoteById(id);
    }

    /*
     *
     ****    Note || Task
     *
     */


    @GetMapping("/labels/")
    public List<NoteLabel> findAllNoteLabel() {
        return noteService.findAllNoteLabel();
    }

    @PostMapping("/labels/")
    public List<NoteLabel> saveNoteLabel(@RequestBody NoteLabel entity){
        return noteService.saveNoteLabel(entity);
    }

    @DeleteMapping("/labels/id/{id}")
    public void deleteNoteLabelById(@PathVariable Long id) {
        noteService.deleteNoteLabelById(id);
    }

    @GetMapping("/tasks/")
    public List<NoteTask> findAllNoteTask() {
        return noteService.findAllNoteTask();
    }

    @PostMapping("/tasks/")
    public NoteTask saveNoteTask(@RequestBody NoteTask entity) throws Exception {
        return noteService.saveNoteTask(entity);
    }

    @DeleteMapping("/tasks/id/{id}")
    public void deleteNoteTaskById(@PathVariable Long id) {
        noteService.deleteNoteTaskById(id);
    }

    @GetMapping("/findByCriteria/{companyId}/{text}")
    public List<Note> findByCriteria(@PathVariable String text,@PathVariable Long companyId) {
        return noteService.findByCriteria(text, companyId);
    }
}
