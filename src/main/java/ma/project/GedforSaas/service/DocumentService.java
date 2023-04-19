package ma.project.GedforSaas.service;

import com.google.gson.Gson;
import ma.project.GedforSaas.abstractSupperClasses.AbstractService;
import ma.project.GedforSaas.configuration.ConstantStaticStrings;
import ma.project.GedforSaas.exception.ResourceNotFoundExceptionConstimized;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static ma.project.GedforSaas.controller.TranslateController.LOCALE;
import static ma.project.GedforSaas.utils.i18n.Translator.TO_LOCALE;
import static ma.project.GedforSaas.utils.i18n.TranslatorCode.ID_NOT_FOUND;

import ma.project.GedforSaas.repository.DocumentRepository;
import ma.project.GedforSaas.model.Document;
import ma.project.GedforSaas.model.FileDB;
import ma.project.GedforSaas.model.Folder;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private FolderService folderService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private AclService aclService;


    @PreAuthorize("@aclPermissionEvaluator.hasPermission(#folder, 'WRITE')")
    public Document addDocument(Document document, @Param("folder") Folder folder) {

        // save the document with its relation in the database
        return documentRepository.save(document);

    }

    public Document getDocumentById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExceptionConstimized(ConstantStaticStrings.CATEGORY_NOT_FOUND));
    }

    public List<Document> findAllDocuments() {

        return documentRepository.findAll();


    }

    public List<Document> findAllDocumentsOnTrash() {

        return documentRepository.findAll();
    }

    // update document ben ms
    public Document updateDocumentWith1file(Long id, String documentString, @RequestParam("file") MultipartFile file)
            throws IOException {

        Document documentInDB = this.getDocumentById(id);
        Document document = new Gson().fromJson(documentString, Document.class);

        FileDB fileDb = fileStorageService.store(file, document.getFolder());

        documentInDB.setContent(document.getContent());
        documentInDB.setName(document.getName());
        documentInDB.setPrincipaleFile(fileDb);

        return documentRepository.save(documentInDB);
    }

    public Document updateDocument(Document documentOld, String documentString, @RequestParam(value = "files", required = false) MultipartFile[] arrayMultipartFile) {
        Document documentNew = new Gson().fromJson(documentString, Document.class);

        documentOld.setContent(documentNew.getContent());
        documentOld.setName(documentNew.getName());

        List<String> filesNames = new ArrayList<>();
        List<FileDB> listFileDb = new ArrayList<>();
        AtomicReference<FileDB> principalFile = new AtomicReference<>(new FileDB());
        //convert documentString to json object
        //  Document document = new Gson().fromJson(documentString, Document.class);
        if (arrayMultipartFile != null && arrayMultipartFile.length != 0) {
            Arrays.asList(arrayMultipartFile).stream().forEach(file -> {
                try {
                    FileDB filedb = fileStorageService.store(file, documentNew.getFolder());
                    listFileDb.add(filedb);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                filesNames.add(file.getOriginalFilename());
            });
            //passer ce variable dans l'objet de document

            principalFile.set(listFileDb.get(0));
            listFileDb.remove(0);
            documentOld.setSecondaryFiles(listFileDb);
            documentOld.setPrincipaleFile(principalFile.get());

            return documentRepository.save(documentOld);

        }
        return documentOld;

    }

    //	public void changeStatut(Long id,String statut) {
//		Document documentInDB = getDocumentById(id);
//		documentInDB.setStatut(statut);
//	}
    public Document findDocumentById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExceptionConstimized(TO_LOCALE(ID_NOT_FOUND, LOCALE)));
    }

    @Transactional
    public void deleteDocument(Long id) {
        documentRepository.deleteById(id);
    }


    @PreAuthorize("@aclPermissionEvaluator.hasPermission(#document, 'DELETE')")
    public Document addDocumentOnTrash(Document document) {
        if (document.isTrash()) {
            document.setTrash(false);
            return documentRepository.save(document);
        }
        document.setTrash(true);
        return documentRepository.save(document);
    }

//    // update document ben ms
//    public Document updateDocument(Document document) {
//        return documentRepository.save(document);
//    }

    public List<Document> addMultipleToFavorite(List<Document> documents) {
        List<Document> documentList = new ArrayList<>();
        for (Document d : documents) {
            Document document = this.findDocumentById(d.getId());
            document.setFavorite(true);
            documentList.add(documentRepository.save(document));
        }
        return documentList;
    }

    public List<Document> addMultipleToFolder(List<Document> documents, Long folder) {
        List<Document> documentList = new ArrayList<>();
        Folder folder1 = this.folderService.findFolderById(folder);
        for (Document d : documents) {
            Document document = this.findDocumentById(d.getId());
            document.setFolder(folder1);
            documentList.add(documentRepository.save(document));
        }
        return documentList;
    }

    public boolean deleteMultiple(List<Document> documents) {
        for (Document d : documents) {
            this.deleteDocument(d.getId());
        }
        return true;
    }


    public List<Document> findByCompanyIdAndTrash(Long id, boolean trash) {
        return documentRepository.findByCompanyIdAndTrash(id, trash);
    }

    public List<Document> findByCompanyId(Long id) {
        return documentRepository.findByCompanyId(id);
    }

    public List<Document> findByCompanyIdAndFolderId(Long companyId, Long folderId) {
        return documentRepository.findByCompanyIdAndFolderId(companyId, folderId);
    }

    public List<Document> findByFolderId(Long folderId) {
        return documentRepository.findByFolderId(folderId);
    }

    public List<Document> findByCompanyIdAndLabelId(Long companyId, Long labelId) {
        return documentRepository.findByCompanyIdAndLabelId(companyId, labelId);
    }

    //TODO: document : find by id to get files of one document
    public Object findDocumentFiles(Long id) {
        Document document = documentRepository.findDocumentContent(id);
        Map<String, Object> files = new HashMap<>();
        files.put("principaleFile", document.getPrincipaleFile());
        files.put("secondaryFiles", document.getSecondaryFiles());
        return files;
    }

    //TODO: document : find by id to get attributs document
    public Object findAttributsDocument(Long id) {
        Document document = documentRepository.findAttributs(id);
        HashMap<String, Object> attributs = new HashMap<>();
        //convert ContentJson to java object
        Object content = new Gson().fromJson(document.getContent(), Object.class);
        attributs.put("content", content);
        return attributs.get("content");
    }

    //TODO: document : find by id to get name
    public String findDocumentToGetName(Long id) {
        return documentRepository.findByIdToGetName(id);
    }

    //TODO: document : change statut after launch
    public Object chnageStatut(Long id, String statut) {
        Document getDocument = getDocumentById(id);
        getDocument.setStatut(statut);
        Document newStatut = documentRepository.save(getDocument);
        HashMap<String, String> document = new HashMap<>();
        document.put("message", "statut is changed");
        document.put("statut", newStatut.getStatut());
        return document;
    }


    public List<Document> findByCriteria(String value, Long id) {
        String query = AbstractService.init(id, "Document"); // here we add table name be careful to make first letter Uppercase
        query += AbstractService.addConstraint("o", "name", value, "LIKE", "");
        query += AbstractService.addConstraint("o", "type", value, "LIKE", "OR");
        query += AbstractService.addConstraint("o", "content", value, "LIKE", "OR");
        query += AbstractService.addConstraint("o", "label.title", value, "LIKE", "OR");
        query += AbstractService.addConstraint("o", "principaleFile.name", value, "LIKE", "OR");
        query += AbstractService.addConstraint("o", "principaleFile.type", value, "LIKE", "OR");
        query += AbstractService.addConstraint("o", "folder.name", value, "LIKE", "OR");
        query += AbstractService.addConstraint("o", "folder.folderType", value, "LIKE", "OR");
        query += AbstractService.addConstraint("o", "docType.name", value, "LIKE", "OR");
        System.out.println(query);
        return entityManager.createQuery(query).getResultList();
    }

    @Autowired
    private EntityManager entityManager;


//	public Document updateDocument(Document document) {
//		// TODO Auto-generated method stub
//		return null;
//	}
}
