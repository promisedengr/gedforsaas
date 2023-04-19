package ma.project.GedforSaas.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ma.project.GedforSaas.model.Folder;
import ma.project.GedforSaas.service.FolderService;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/v1/folder")
public class FolderController {

    private final FolderService folderService;

    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Folder>> getAllFolders() {
        List<Folder> folders = folderService.findAllFolders();
        return new ResponseEntity<>(folders, HttpStatus.OK);
    }

    @GetMapping("/{trash}/company/id/{id}")
    public List<Folder> findByCompanyId(@PathVariable Long id,@PathVariable("trash")  boolean trash) {
        return folderService.findByCompanyId(id, trash);
    }
    @GetMapping("/find/subfolders/{id}")
    public List<Folder> findByParentFolderId(@PathVariable Long id) {
        return folderService.findByParentFolderId(id);
    }

    @GetMapping("/find/{id}")
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
    public ResponseEntity<Folder> updateFolder(@RequestBody Folder folder) {
        Folder updateFolder = folderService.updateFolder(folder);
        return new ResponseEntity<>(updateFolder, HttpStatus.OK);
    }


    @DeleteMapping("/id/{id}")
    public boolean deleteFolder(@PathVariable("id") Long id) {
        Folder folder = this.folderService.findFolderById(id);
        return folderService.deleteFolder(folder);
    }




}
