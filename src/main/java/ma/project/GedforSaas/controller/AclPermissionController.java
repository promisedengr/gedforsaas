package ma.project.GedforSaas.controller;

import ma.project.GedforSaas.model.Document;
import ma.project.GedforSaas.model.Folder;
import ma.project.GedforSaas.repository.DocumentRepository;
import ma.project.GedforSaas.repository.FolderRepository;
import ma.project.GedforSaas.service.AclService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("admin/api/v1/acl/permission")
public class AclPermissionController {

    @Autowired
    private AclService aclService;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private FolderRepository folderRepository;


    @GetMapping("/id/{id}/mask/{mask}")
    public int update_permission(@PathVariable Integer id,@PathVariable Integer mask) {
        return aclService.update_permission(id, mask);
    }

    @GetMapping("/id/{id}")
    public Object get_acl_object_by_id(@PathVariable String id) {
        return aclService.get_acl_object_by_id(id);
    }

    @GetMapping("/folder/{folderId}/{username}/{mask}")
    public boolean set_permission_for_folders_object(@PathVariable String folderId, @PathVariable Integer mask, @PathVariable String username) throws Exception {
        Optional<Folder> folder = folderRepository.findById(Long.parseLong(folderId));
        if (folder.isPresent()) return aclService.set_permission_for_folders_object(folder.get(), username,mask);
        else throw new Exception("Folder not found .");
    }


    @GetMapping("/document/{docId}/{username}/{mask}")
    public boolean set_permission_for_docs_object(@PathVariable String docId,@PathVariable Integer mask, @PathVariable String username) throws Exception {
        Optional<Document> document = documentRepository.findById(Long.parseLong(docId));
        if (document.isPresent()) return aclService.set_permission_for_docs_object(document.get(), username,mask);
        else throw new Exception("Document not found.");
    }


    @GetMapping("/folder/id/{object_id_identity}")
    public Object get_acl_object_for_folder(@PathVariable String object_id_identity) {
        Integer acl_class_id = this.aclService.get_acl_class_by_class("ma.project.GedforSaas.folder.Folder");
        return aclService.get_acl_object(String.valueOf(acl_class_id), object_id_identity);
    }

    @GetMapping("/document/id/{object_id_identity}")
    public Object get_acl_object_for_doc(@PathVariable String object_id_identity) {
        Integer acl_class_id = this.aclService.get_acl_class_by_class("ma.project.GedforSaas.document.Document");
        return aclService.get_acl_object(String.valueOf(acl_class_id), object_id_identity);
    }


    @GetMapping("/folder/folderId/{object_id_identity}")
    public List<Object> get_permissions_folder(@PathVariable String object_id_identity) {
        Integer acl_class_id = this.aclService.get_acl_class_by_class("ma.project.GedforSaas.folder.Folder");
        Integer acl_object_identity = aclService.get_acl_object_by_object_id_class_and_object_id_identity(String.valueOf(acl_class_id), object_id_identity);
        return aclService.get_acl_entry_by_acl_object_identity(String.valueOf(acl_object_identity));
    }

    @GetMapping("/document/documentId/{object_id_identity}")
    public List<Object> get_permissions_document(@PathVariable String object_id_identity) {
        Integer acl_class_id = this.aclService.get_acl_class_by_class("ma.project.GedforSaas.document.Document");
        Integer acl_object_identity = aclService.get_acl_object_by_object_id_class_and_object_id_identity(String.valueOf(acl_class_id), object_id_identity);
        return aclService.get_acl_entry_by_acl_object_identity(String.valueOf(acl_object_identity));
    }
}
