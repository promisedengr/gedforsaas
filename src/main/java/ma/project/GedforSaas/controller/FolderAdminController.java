package ma.project.GedforSaas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import ma.project.GedforSaas.model.Folder;
import ma.project.GedforSaas.service.FolderService;

import java.util.List;
@RestController
@RequestMapping("admin/api/v1/folder")
public class FolderAdminController {

    @Autowired
    private  FolderService folderService;

    @GetMapping("/all")
    public ResponseEntity<List<Folder>> getAllFolders() {
        List<Folder> folders = folderService.findAllFolders();
        return new ResponseEntity<>(folders, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Folder> getFolderById(@PathVariable("id") Long id) {
        Folder folder = folderService.findFolderById(id);
        return new ResponseEntity<>(folder, HttpStatus.OK);
    }

    @PostMapping("/add")
    public Folder addFolder(@RequestBody Folder folder) {
        if (folder.getParentFolderId() != null){
            Folder parentFolder = this.folderService.findFolderById(folder.getParentFolderId());
            return folderService.addFolder(folder, parentFolder);
        } else {
            return folderService.saveFolder(folder);
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasPermission(#folder, 'WRITE')")
    public Folder updateFolder(@RequestBody Folder folder) {
        System.out.println(folder.getId());
        return folderService.updateFolder(folder);
    }

    @GetMapping("/{trash}/company/id/{id}")
    public List<Folder> findByCompanyId(@PathVariable Long id,@PathVariable("trash")  boolean trash) {
        return folderService.findByCompanyId(id, trash);
    }

    @GetMapping("/name/{name}")
    public Folder findByName(@PathVariable String name) {
        return folderService.findByName(name);
    }

    @GetMapping("/find/subfolders/{id}")
    public List<Folder> findByParentFolderId(@PathVariable Long id) {
        return folderService.findByParentFolderId(id);
    }

    @DeleteMapping("/id/{action}/{id}")
    public boolean deleteFolder(@PathVariable("id") Long id,@PathVariable("action")  String action) {
        Folder folder = this.folderService.findFolderById(id);
        return folderService.addToTrashorDelete(folder, !folder.isTrash(), action);
    }

    @GetMapping("/doctype/id/{id}")
    public List<Folder> findByDocTypeId(@PathVariable Long id) {
        return folderService.findByDocTypeId(id);
    }
}
