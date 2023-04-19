package ma.project.GedforSaas.service;

import ma.project.GedforSaas.model.Company;
import ma.project.GedforSaas.repository.DocumentRepository;
import ma.project.GedforSaas.repository.FolderRepository;
import ma.project.GedforSaas.model.Document;
import ma.project.GedforSaas.model.Folder;
import ma.project.GedforSaas.exception.ResourceNotFoundExceptionConstimized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static ma.project.GedforSaas.controller.TranslateController.LOCALE;
import static ma.project.GedforSaas.utils.i18n.Translator.TO_LOCALE;
import static ma.project.GedforSaas.utils.i18n.TranslatorCode.ID_NOT_FOUND;

@Service
@Transactional
public class FolderService {
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private AclService aclService;
    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    public FolderService(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    public Folder findByName(String name) {
        return folderRepository.findByName(name);
    }

    public List<Folder> findByCompanyId(Long id, boolean trash) {
        return folderRepository.findByCompanyIdAndTrashAndParentFolderId(id, trash, null);
    }


    @PreAuthorize("@aclPermissionEvaluator.hasPermission(#parentFolder, 'WRITE')")
    public Folder addFolder(Folder folder, Folder parentFolder) {
        Company company = this.companyService.findCompanyById(folder.getCompany().getId());
        folder.setCompany(company);
        folder.setFolder(parentFolder);
        Folder folder1 = folderRepository.save(folder);


        /**
         *  save folder object in acl
         */
        addToAcl(folder, folder1);

        return folder1;
    }

    public Folder saveFolder(Folder folder) {
        Company company = this.companyService.findCompanyById(folder.getCompany().getId());
        folder.setCompany(company);
        Folder folder1 = folderRepository.save(folder);


        /**
         *  save folder object in acl
         */
        addToAcl(folder, folder1);

        return folder1;
    }

    public List<Folder> findByDocTypeId(Long id) {
        return folderRepository.findByDocTypeId(id);
    }

    private void addToAcl(Folder folder, Folder folder1) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Integer acl_class_id = this.aclService.get_acl_class_by_class("ma.project.GedforSaas.folder.Folder");
        Integer sid = this.aclService.get_acl_sid_by_sid(name);

        if (folder.getParentFolderId() != null) {
            Integer parent_object_id = this.aclService.get_acl_object_by_object_id_identity(String.valueOf(folder.getParentFolderId()), acl_class_id);

            this.aclService.insert_acl_object_identity(String.valueOf(acl_class_id), folder1.getId().toString(),
                    String.valueOf(parent_object_id), String.valueOf(sid), true);
        } else {
            this.aclService.insert_acl_object_identity(String.valueOf(acl_class_id), folder1.getId().toString(),
                    null, String.valueOf(sid), true);
        }
        Integer alc_object_id = this.aclService.get_acl_object_by_object_id_class_and_object_id_identity(String.valueOf(acl_class_id), folder1.getId().toString());
        this.aclService.insert_acl_entry(String.valueOf(alc_object_id), 1, sid, 16, true);// ADMIN
    }


    public List<Folder> findAllFolders() {
        return folderRepository.findAll();
    }

    @PreAuthorize("@aclPermissionEvaluator.hasPermission(#folder, 'CREATE')")
    public Folder updateFolder(Folder folder) {
        System.out.println(folder.getId());
        Company company = this.companyService.findCompanyById(folder.getCompany().getId());
        folder.setCompany(company);
        return folderRepository.save(folder);
    }

    public Folder findFolderById(Long id) {
        return folderRepository.findFolderById(id)
                .orElseThrow(() -> new ResourceNotFoundExceptionConstimized(TO_LOCALE(ID_NOT_FOUND, LOCALE)));
    }

    /**
     * get list of subfolders
     *
     * @param id
     * @return
     */
    public List<Folder> findByParentFolderId(Long id) {
        return folderRepository.findByFolderId(id);
    }


    @PreAuthorize("@aclPermissionEvaluator.hasPermission(#folder, 'DELETE')")
    @Transactional
    public boolean deleteFolder(Folder folder) {
        folderRepository.deleteById(folder.getId());
        return true;
    }

    /**
     * @param folder
     * @param trash
     * @param action => TRASH | DELETE
     */
    @PreAuthorize("@aclPermissionEvaluator.hasPermission(#folder, 'DELETE')")
    public boolean addToTrashorDelete(Folder folder, boolean trash, String action) {

        if ("TRASH".equals(action)) {
            //1- add folder to trash change trash to true
            //2- get list of subfolder using method findByParentFolderId(folderID)
            //3-  add every folder in list of subFolders to trash
            //4- getList of docs by folderID ( methods exist in docService)
            folder.setTrash(trash);
            folderRepository.save(folder);
            List<Folder> subFolders = findByParentFolderId(folder.getId());
            // add condition
            if (subFolders != null) {
                for (Folder folder2 : subFolders) {
                    folder2.setTrash(trash);
                    folderRepository.save(folder2);
                }
            }
            List<Document> documents = documentRepository.findAllByFolderId(folder.getId());
            if (documents != null) {
                for (Document document : documents) {
                    document.setTrash(trash);
                    documentRepository.save(document);
                }
            }

        } else if ("DELETE".equals(action)) {
            List<Folder> subFolders = findByParentFolderId(folder.getId());
            // add condition
            if (subFolders != null) {
                for (Folder folder2 : subFolders) {
                    folder2.setTrash(trash);
                    folderRepository.deleteById(folder2.getId());
                }
            }
            folderRepository.deleteById(folder.getId());
        }
        return true;
    }


}
