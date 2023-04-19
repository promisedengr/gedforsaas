package ma.project.GedforSaas.controller;

import ma.project.GedforSaas.model.Document;
import ma.project.GedforSaas.model.Folder;
import ma.project.GedforSaas.service.AclService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin/api/v1/acl")
public class AclController {
    /**
     * *********************************************** INSERT METHODS ******************************************************
     */

    @Autowired
    private AclService aclService;

    @PostMapping("/insert_acl_entry")
    public void insert_acl_entry(@RequestParam("acl_object_identity") String acl_object_identity,
                                 @RequestParam("aceOrder") Integer aceOrder,
                                 @RequestParam("sid") Integer sid,
                                 @RequestParam("mask") Integer mask,
                                 @RequestParam("granting") boolean granting) {
        aclService.insert_acl_entry(acl_object_identity, aceOrder, sid, mask, granting);
    }


    @PostMapping("/insert_acl_class")
    public void insert_acl_class(@RequestParam("acl_object_class") String acl_object_class) {
        aclService.insert_acl_class(acl_object_class);
    }

    @PostMapping("/insert_acl_sid")
    public void insert_acl_sid(@RequestParam("principal") String principal,
                               @RequestParam("sid") String sid) {
        aclService.insert_acl_sid(principal, sid);
    }

    @PostMapping("/insert_acl_object_identity")
    public void insert_acl_object_identity(@RequestParam("object_id_class") String object_id_class,
                                           @RequestParam("object_id_identity") String object_id_identity,
                                           @RequestParam("parent_object") String parent_object,
                                           @RequestParam("owner_sid") String owner_sid,
                                           @RequestParam("entries_inheriting") boolean entries_inheriting) {
        aclService.insert_acl_object_identity(object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting);
    }

    /**
     * *********************************************** GET METHODS ******************************************************
     */

    @GetMapping("/get_acl_entry")
    public List<Object> get_acl_entry() {
        return aclService.get_acl_entry();
    }

    @GetMapping("/get_acl_entry/{sid}")
    public List<Object> get_acl_entry_using_sid(@PathVariable Integer sid) {
        return aclService.get_acl_entry_using_sid(sid);
    }


    @GetMapping("/get_acl_class")
    public List<Object> get_acl_class() {
        return aclService.get_acl_class();
    }


    @GetMapping("/get_acl_class/{acl_object_class}")
    public Integer get_acl_class_by_class(@PathVariable String acl_object_class) {
        return aclService.get_acl_class_by_class(acl_object_class);
    }

    @GetMapping("/get_acl_sid")
    public List<Object> get_acl_sid() {
        return aclService.get_acl_sid();
    }


    @GetMapping("/get_acl_object_identity")
    public List<Object> get_acl_object_identity() {
        return aclService.get_acl_object_identity();
    }

    /**
     * *********************************************** UPDATES METHODS ******************************************************
     */

    @PostMapping("/update_granting/folder/{id}/{granting}")
    @PreAuthorize("hasPermission(#object, 'ADMINISTRATION')")
    public int update_granting_acl_entry_for_folder(@RequestBody Folder object,
                                         @PathVariable Integer id,
                                         @PathVariable boolean granting) {
        return aclService.update_granting_acl_entry(id, granting);
    }

    @PostMapping("/update_granting/document/{id}/{granting}")
    @PreAuthorize("hasPermission(#object, 'ADMINISTRATION')")
    public int update_granting_acl_entry_fol_doc(@RequestBody Document object,
                                         @PathVariable Integer id,
                                         @PathVariable boolean granting) {
        return aclService.update_granting_acl_entry(id, granting);
    }



    @PostMapping("/update_inheriting/document/{id}/{entries_inheriting}")
    @PreAuthorize("hasPermission(#object, 'ADMINISTRATION')")
    public int update_entries_inheriting_acl_object_identity_fol_doc(@RequestBody Document object,
                                                             @PathVariable Integer id,
                                                             @PathVariable boolean entries_inheriting) {
        return aclService.update_entries_inheriting_acl_object_identity(id, entries_inheriting);
    }



    @PostMapping("/update_inheriting/folder/{id}/{entries_inheriting}")
    @PreAuthorize("hasPermission(#object, 'ADMINISTRATION')")
    public int update_entries_inheriting_acl_object_identity_fol_folder(@RequestBody Folder object,
                                                             @PathVariable Integer id,
                                                             @PathVariable boolean entries_inheriting) {
        System.out.println(id);
        return aclService.update_entries_inheriting_acl_object_identity(id, entries_inheriting);
    }
}
