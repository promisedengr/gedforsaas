package ma.project.GedforSaas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ma.project.GedforSaas.model.Note;
import ma.project.GedforSaas.service.NoteService;

import java.util.List;

@RestController
@RequestMapping("api/v1/note/admin")
public class NoteAdminController {

    @Autowired
    private NoteService noteService;


    @GetMapping("/company/id/{id}")
    public List<Note> findByCompanyId(@PathVariable Long id) {
        return noteService.findByCompanyId(id);
    }


    @PostMapping("/save")
    public Note saveNote(@RequestBody Note note) throws Exception {
       return noteService.saveNote(note);
    }

    @PutMapping("/update")
    public void updateNote(@RequestBody Note note) throws Exception {
        noteService.saveNote(note);
    }

    @GetMapping("/findAll")
    public List<Note> findAllNotes() {
        return noteService.findAllNotes();
    }

    @DeleteMapping("/delete/{id}")
    public boolean deleteById(@PathVariable Long id) {
       return noteService.deleteNoteById(id);
    }


    @GetMapping("/findByCriteria/{companyId}/{text}")
    public List<Note> findByCriteria(@PathVariable String text,@PathVariable Long companyId) {
        return noteService.findByCriteria(text, companyId);
    }

}

