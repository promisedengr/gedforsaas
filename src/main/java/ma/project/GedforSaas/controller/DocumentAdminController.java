package ma.project.GedforSaas.controller;

import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ma.project.GedforSaas.model.Document;
import ma.project.GedforSaas.model.FileDB;
import ma.project.GedforSaas.model.OcrContent;
import ma.project.GedforSaas.model.Template;
import ma.project.GedforSaas.service.AclService;
import ma.project.GedforSaas.service.DocumentService;
import ma.project.GedforSaas.service.FileStorageService;
import ma.project.GedforSaas.service.TemplateService;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("admin/api/v1/document")
public class DocumentAdminController {
    @Autowired
    private DocumentService documentService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private AclService aclService;

    @Autowired
    private TemplateService templateService;

//    private MyAclService myaclService;

    @Autowired
    private RestTemplate restTemplate;
    //    @Autowired
//    private static ConstantStaticStrings constantStaticString;
    @Value("${OCR_URL_MS}")
    private String OCR_URL_MS;
    @Value("${OCR_URL_IMAGE}")
    private String OCR_URL_IMAGE;
    @Value("${OCR_URL_PDF}")
    private String OCR_URL_PDF;

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @GetMapping("/all")
    public List<Document> getAllDocuments() {
        return documentService.findAllDocuments();
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable("id") Long id) {
        Document document = documentService.findDocumentById(id);
        return new ResponseEntity<>(document, HttpStatus.OK);
    }

    @PostMapping("/add")
    public Document addDocument(@RequestParam("doc") String documentString,
                                @RequestParam(value = "files", required = false) MultipartFile file,
                                @RequestParam("user") String username,
                                @RequestParam(value = "content", required = false) String content) throws IOException {
        FileDB fileDB = null;

        System.out.println(content);

        // transformer le document du format string vers format document json
        // Document document = convertStringToDocument(documentString);
        Document document = new Gson().fromJson(documentString, Document.class);
        Long ID = document.getId();
        if (file != null) {
            // ajouter le file dans la base de données
            fileDB = fileStorageService.store(file, document.getFolder());
            document.setContentMimeType(file.getContentType());
        }

        // passer ce variable dans l'objet de document
        document.setPrincipaleFile(fileDB);

        // sauvegarder ce objet dans la base de données
        // get the template from the database with the id mentioned in object document
        if (document.getTemplate() != null) {
            Template template = templateService.findTemplateById(document.getTemplate().getId());
            // set the template object in the document object
            document.setTemplate(template);
            document.setContent(content);
        }
        if (document.getLabel() == null) {
            document.setLabel(null);
        } else {
            if (document.getLabel().getId() == null) {
                document.setLabel(null);
            }
        }

        if (document.getDoctype() == null) {
            document.setDoctype(null);
        } else {
            if (document.getDoctype().getId() == null) {
                document.setDoctype(null);
            } else {
                document.setContent(content);
            }
        }

        document.setCreatedBy(username);
        document.setLastModifiedBy(username);

        Document document1 = documentService.addDocument(document, document.getFolder());
        /**
         * save folder object in acl
         *
         */
        String iddoc = document1.getId().toString();
        System.out.println(iddoc);
        MultipartFile[] files = {file};
        System.out.println(ID);
        if (ID == null) {
            addToAcl(username, document1);
        }
        return document1;
    }

    private void addToAcl(String username, Document document1) {
        Integer acl_class_id = this.aclService.get_acl_class_by_class("ma.project.GedforSaas.document.Document");
        Integer acl_class_id_folder = this.aclService.get_acl_class_by_class("ma.project.GedforSaas.folder.Folder");

        Integer sid = this.aclService.get_acl_sid_by_sid(username);

        if (document1.getFolder() != null) {
            Integer parent_object_id = this.aclService.get_acl_object_by_object_id_identity(
                    String.valueOf(document1.getFolder().getId()), acl_class_id_folder);
            this.aclService.insert_acl_object_identity(String.valueOf(acl_class_id), document1.getId().toString(),
                    String.valueOf(parent_object_id), String.valueOf(sid), true);
        } else {
            this.aclService.insert_acl_object_identity(String.valueOf(acl_class_id), document1.getId().toString(), null,
                    String.valueOf(sid), true);
        }
        Integer alc_object_id = this.aclService.get_acl_object_by_object_id_class_and_object_id_identity(
                String.valueOf(acl_class_id), document1.getId().toString());

        this.aclService.insert_acl_entry(String.valueOf(alc_object_id), 1, sid, 16, true); // ADMIN
    }

    @PostMapping("/adds")
    public ResponseEntity<Document> addDocumentJoin(@RequestParam("doc") String documentString,
                                                    @RequestParam(value = "files", required = false) MultipartFile[] arrayMultipartFile,
                                                    @RequestParam("user") String username,
                                                    @RequestParam(value = "content", required = false) String content) {

        try {
            List<String> filesNames = new ArrayList<>();
            List<FileDB> listFileDb = new ArrayList<>();
            AtomicReference<FileDB> principalFile = new AtomicReference<>(new FileDB());
            // convert documentString to json object
            Document document = new Gson().fromJson(documentString, Document.class);
            Long ID = document.getId();
            if (arrayMultipartFile != null && arrayMultipartFile.length != 0) {
                Arrays.asList(arrayMultipartFile).stream().forEach(file -> {
                    try {
                        FileDB filedb = fileStorageService.store(file, document.getFolder());
                        listFileDb.add(filedb);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    filesNames.add(file.getOriginalFilename());
                });
                // passer ce variable dans l'objet de document

                principalFile.set(listFileDb.get(0));
                listFileDb.remove(0);
                document.setSecondaryFiles(listFileDb);
                document.setPrincipaleFile(principalFile.get());
            }

            if (document.getTemplate() != null) {
                Template template = templateService.findTemplateById(document.getTemplate().getId());
                // set the template object in the document object
                document.setTemplate(template);
                document.setContent(content);
            }

            if (document.getLabel() == null) {
                document.setLabel(null);
            } else {
                if (document.getLabel().getId() == null) {
                    document.setLabel(null);
                }
            }

            if (document.getDoctype() == null) {
                document.setDoctype(null);
            } else {
                if (document.getDoctype().getId() == null) {
                    document.setDoctype(null);
                } else {
                    document.setContent(content);
                }
            }
            // sauvegarder ce objet dans la base de données

            Document document1 = documentService.addDocument(document, document.getFolder());
            /**
             * save folder object in acl
             *
             */
            String id = document1.getId().toString();
            // ocrprocess(arrayMultipartFile, id);
            System.out.println(ID);
            if (ID == null) {
                addToAcl(username, document1);
            }

            return new ResponseEntity<>(document1, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }


    @PostMapping("/trash")
    public Document addDocumentOnTrash(@RequestBody Document document) {
        return documentService.addDocumentOnTrash(document);
    }

    @PutMapping("/update")
    public ResponseEntity<Document> updateDocument(@RequestBody Document document,
                                                   @RequestParam("doc") String documentString,
                                                   @RequestParam(value = "files", required = false) MultipartFile[] arrayMultipartFile) throws IOException {
        Document updateDocument = documentService.updateDocument(document, documentString, arrayMultipartFile);
        return new ResponseEntity<>(updateDocument, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable("id") Long id) {
        documentService.deleteDocument(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/company/id/{id}/{trash}")
    public List<Document> findByCompanyId(@PathVariable Long id, @PathVariable boolean trash) {
        return documentService.findByCompanyIdAndTrash(id, trash);
    }

    @GetMapping("/company/id/{companyId}/folder/id/{folderId}")
    public List<Document> findByCompanyIdAndFolderId(@PathVariable Long companyId, @PathVariable Long folderId) {
        return documentService.findByCompanyIdAndFolderId(companyId, folderId);
    }

    @GetMapping("/company/id/{companyId}/label/id/{labelId}")
    public List<Document> findByCompanyIdAndLabelId(@PathVariable Long companyId, @PathVariable Long labelId) {
        return documentService.findByCompanyIdAndLabelId(companyId, labelId);
    }

    @GetMapping("/company/id/{id}/trash/{trash}")
    public List<Document> findByCompanyIdAndTrash(@PathVariable Long id, @PathVariable boolean trash) {
        return documentService.findByCompanyIdAndTrash(id, trash);
    }

    @GetMapping("/criteria/{companyId}/{value}")
    public List<Document> findByCriteria(@PathVariable String value, @PathVariable Long companyId) {

        List<Document> documentList = new ArrayList<Document>();
        Object from_elastic_search = restTemplate
                .exchange(OCR_URL_MS + "findbytext/" + value, HttpMethod.GET, null, String.class)
                .getBody();
        System.out.println("value come from elastic " + from_elastic_search);
        List<Object> to_object_array = Arrays.asList(from_elastic_search);
        // System.out.println(wordList.get(0));
        // from json array to json object array
        OcrContent[] ocrcontent = (OcrContent[]) new Gson().fromJson((String) to_object_array.get(0),
                OcrContent[].class);
        for (OcrContent ocr : ocrcontent) {
            Long id = Long.parseLong(ocr.getDocument_id());
            try {
                documentList.add(documentService.findDocumentById(id));
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(ocr.getDocument_id());
        }
////        OcrContent ocrcontent = new Gson().fromJson((String) from_elastic_search, OcrContent.class);
        // System.out.println("OcrContent" +ocrcontent);
        documentList.addAll(documentService.findByCriteria(value, companyId));
        return documentList;
    }

    @PostMapping("/multiple/favorite")
    public List<Document> addMultipleToFavorite(@RequestBody List<Document> documents) {
        return documentService.addMultipleToFavorite(documents);
    }

    @PostMapping("/multiple/folder/{folder}")
    public List<Document> addMultipleToFolder(@RequestBody List<Document> documents, @PathVariable Long folder) {
        return documentService.addMultipleToFolder(documents, folder);
    }

    @PostMapping("/multiple/delete/")
    public boolean deleteMultiple(@RequestBody List<Document> documents) {
        return documentService.deleteMultiple(documents);
    }

    // Elasticsearch

    @GetMapping("/ocr")
    public String fetchAll() {
        return restTemplate.exchange(OCR_URL_MS + "all", HttpMethod.GET, null, String.class).getBody();
    }

//    @PostMapping("/addOcr")
//    public String addSomething(@RequestBody OcrContent ocrContent) {
//    	return restTemplate.postForObject(OCR_URL_MS+"add" , ocrContent , String.class);
//    }

    @GetMapping("/findbyidocr/{id}")
    public String fechById(@PathVariable Long id) {
        return restTemplate
                .exchange(OCR_URL_MS + "find/" + id, HttpMethod.GET, null, String.class)
                .getBody();
//    	System.out.println(ocrContent);
//    	 restTemplate.getForObject(OCR_URL_MS + "find/" +id , String.class);
    }

    @GetMapping("/findbynameocr/{filename}")
    public String findByFileName(@PathVariable String filename) {
        return restTemplate.exchange(OCR_URL_MS + "findbyfilename/" + filename, HttpMethod.GET,
                null, String.class).getBody();
//    	System.out.println(ocrContent);

    }

    @GetMapping("/findbytextocr/{text}")
    public String findByText(@PathVariable String text) {
        return restTemplate.exchange(OCR_URL_MS + "findbytext/" + text, HttpMethod.GET, null, String.class).getBody();
//    	System.out.println(ocrContent);
//    	restTemplate.getForObject(OCR_URL_MS + "findbytext/" +text, String.class);
    }

    // OCR EXTRACT TEXT FROM IMAGE
    @PostMapping("/extractTextFromImage")
    public ResponseEntity<String> extractextfromimages(@RequestParam("file") MultipartFile[] files, String langue) {

        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        String response;
        HttpStatus httpStatus = HttpStatus.CREATED;

        try {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    map.add("file",
                            new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));
                }
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
            response = restTemplate.postForObject(OCR_URL_IMAGE + "extractTextFromImage", requestEntity, String.class);

        } catch (HttpStatusCodeException e) {
            httpStatus = HttpStatus.valueOf(e.getStatusCode().value());
            response = e.getResponseBodyAsString();
        } catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            response = e.getMessage();
        }

        return new ResponseEntity<>(response, httpStatus);

//		return restTemplate.exchange(OCR_URL_IMAGE + "extractTextFromImage",HttpMethod.POST, requestEntity, String.class);
    }

    class MultipartInputStreamFileResource extends InputStreamResource {

        private final String filename;

        MultipartInputStreamFileResource(InputStream inputStream, String filename) {
            super(inputStream);
            this.filename = filename;
        }

        @Override
        public String getFilename() {
            return this.filename;
        }

        @Override
        public long contentLength() throws IOException {
            return -1; // we do not want to generally read the whole stream into memory ...
        }
    }

    private void ocrprocess(MultipartFile[] files, String document_id) {
        System.out.println(files.length);
        System.out.println(document_id);
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        String response;
        HttpStatus httpStatus = HttpStatus.CREATED;
        for (MultipartFile file : files) {
            try {
                map.add("files",
                        new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));
                map.add("id", document_id);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.MULTIPART_FORM_DATA);
                HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
                FileDB fileDB = new FileDB(file.getName(), file.getContentType(), file.getBytes());
                if (fileDB.getType().contentEquals("application/pdf")) {
                    response = restTemplate.postForObject(OCR_URL_PDF + "extractTextFromPDFFile", requestEntity,
                            String.class);
                } else {
                    response = restTemplate.postForObject(OCR_URL_IMAGE + "extractTextFromImage", requestEntity,
                            String.class);
                }

            } catch (HttpStatusCodeException e) {
                httpStatus = HttpStatus.valueOf(e.getStatusCode().value());
                response = e.getResponseBodyAsString();
            } catch (Exception e) {
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                response = e.getMessage();
            }
        }

    }

    // OCR EXTRACT TEXT FROM PDF
    @PostMapping("/extractTextFrompdf")
    public ResponseEntity<String> extractextfrompdf(@RequestParam("file") MultipartFile[] files, String langue) {

        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        String response;
        HttpStatus httpStatus = HttpStatus.CREATED;

        try {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    map.add("file",
                            new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));
                }
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
            response = restTemplate.postForObject(OCR_URL_PDF + "extractTextFromPDFFile", requestEntity, String.class);

        } catch (HttpStatusCodeException e) {
            httpStatus = HttpStatus.valueOf(e.getStatusCode().value());
            response = e.getResponseBodyAsString();
        } catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            response = e.getMessage();
        }

        return new ResponseEntity<>(response, httpStatus);

//		return restTemplate.postForObject(OCR_URL_PDF + "extractTextFromPDFFile", file, String.class);
    }

//	@GetMapping("/export-documents")
//	public void exportCSV(HttpServletResponse response) throws Exception {
//
//		// set file name and content type
//		String filename = "documents.csv";
//
//		response.setContentType("text/csv");
//		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
//
//		// create a csv writer
//		StatefulBeanToCsv<Document> writer = new StatefulBeanToCsvBuilder<Document>(response.getWriter())
//				.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator(CSVWriter.DEFAULT_SEPARATOR)
//				.withOrderedResults(false).build();
//
//		// write all documents to csv file
//		writer.write(documentService.findAllDocuments());
//
//	}

}
