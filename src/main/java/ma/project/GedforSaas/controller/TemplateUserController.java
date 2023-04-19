package ma.project.GedforSaas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ma.project.GedforSaas.model.Template;
import ma.project.GedforSaas.service.TemplateService;

import java.util.List;

@RestController
@RequestMapping("user/api/v1/template")
public class TemplateUserController {
    @Autowired
    private TemplateService templateService;

    @GetMapping("/company/id/{id}")
    public List<Template> findByCompanyId(@PathVariable Long id) {
        return templateService.findByCompanyId(id);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Template>> getAllTemplates() {
        List<Template> templates = templateService.findAllTemplates();

        return new ResponseEntity<>(templates, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Template> getTemplateById(@PathVariable("id") Long id) {
        Template template = templateService.findTemplateById(id);
        return new ResponseEntity<>(template, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Template> addTemplate(@RequestBody Template template) throws Exception {
        Template newTemplate = templateService.addTemplate(template);
        return new ResponseEntity<>(newTemplate, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Template> updateTemplate(@PathVariable("id") Long id, @RequestBody Template template) {

        return new ResponseEntity<>(templateService.updateTemplate(id, template), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTemplate(@PathVariable("id") Long id) {
        templateService.deleteTemplate(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
