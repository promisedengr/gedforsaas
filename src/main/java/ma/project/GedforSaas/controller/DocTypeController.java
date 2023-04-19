package ma.project.GedforSaas.controller;

// import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ma.project.GedforSaas.model.DocType;
import ma.project.GedforSaas.service.DocTypeService;

import java.util.List;

@RestController
@RequestMapping("api/v1/doctype")
public class DocTypeController {

    @GetMapping("/")
    public List<DocType> findAll() {
        return docTypeService.findAll();
    }

    @PostMapping("/save")
    public DocType save(@RequestBody DocType entity) {
        return docTypeService.save(entity);
    }

    @GetMapping("/id/{id}")
    public DocType findById(@PathVariable Long id) throws Exception {
        return docTypeService.findDoctypeById(id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteById(Long id) {
        docTypeService.deleteById(id);
    }


    @GetMapping("/company/{id}")

    public List<DocType> findByCompanyId(@PathVariable Long id) {
        return docTypeService.findByCompanyId(id);
    }

    @Autowired
    private DocTypeService docTypeService;

    // TODO : get all prorietés of a document
    @GetMapping("/proprites/{company}/{id}")
    public Object getProprietes(@PathVariable("company") String company , @PathVariable("id") Long id) throws Exception {
        return  docTypeService.getProprietesService(company, id);
    }

    // TODO : get all types of a document
    @GetMapping("/types")
    public  List<Object> getTypes(){
        return  docTypeService.getTypesService();
    }
}
