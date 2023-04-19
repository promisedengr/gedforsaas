package ma.project.GedforSaas.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.Gson;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import ma.project.GedforSaas.model.FileInfo;
import ma.project.GedforSaas.model.TemplateConfig;
import ma.project.GedforSaas.service.ConfigTemplateService;
import ma.project.GedforSaas.service.TemplateFileStorageService;
import ma.project.GedforSaas.service.TemplateFileStorageServiceImpl;
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@RestController
@RequestMapping("admin/api/v1/uploadTemplate")
public class FilesController {
    @Autowired
    ConfigTemplateService configTemplateService;
    @Autowired
    TemplateFileStorageServiceImpl fileStorageService;
    @Autowired
    TemplateFileStorageService storageService;

    @PostMapping("/upload")
    public TemplateConfig uploadFile(@RequestParam("files") MultipartFile file,
                                     @RequestParam("templateconfig") String templateConfig) throws Exception {
        System.out.println(file.getOriginalFilename());
        System.out.println(file.getName());
        System.out.println(file.getSize());
        TemplateConfig templateconf1 = new Gson().fromJson(templateConfig, TemplateConfig.class);
        TemplateConfig templateconfdb = configTemplateService.addConfig(templateconf1);
        System.out.println(templateconfdb.getId());
        try {
            storageService.save(file, templateconfdb.getId());
            templateconfdb.setTitre(templateconfdb.getId() + "-" + file.getOriginalFilename());
            return this.configTemplateService.addConfig(templateconfdb);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @PutMapping("/update")
    public TemplateConfig updateTemplate(@RequestParam("templateconfig") String templateConfig,
                                         @RequestParam(value = "files", required = false) MultipartFile file) {
        TemplateConfig templateconf1 = new Gson().fromJson(templateConfig, TemplateConfig.class);
        if (file != null) {
            storageService.save(file, templateconf1.getId());
        }
        return configTemplateService.addConfig(templateconf1);
    }


    @GetMapping("/files")
    public ResponseEntity<List<FileInfo>> getListFiles() {
        List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FilesController.class, "downloadFile", path.getFileName().toString()).build().toString();
            return new FileInfo(filename, url);
        }).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    @DeleteMapping("/files")
    public void deleteListFiles() {
        storageService.deleteAll();
    }

    @GetMapping("/all")
    public List<TemplateConfig> findAllTemplate() {
        return configTemplateService.findAllTemplate();
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @GetMapping("/file/{filename}")
    public String getFile(@PathVariable String filename) throws Exception {
        Resource resource = storageService.load(filename);
        File file = this.convertDocxToPdf(resource.getFile());
        System.out.println(file.getName());
        return file.getName();
    }


    @DeleteMapping("/delete/id/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        configTemplateService.deleteById(id);
    }





    @GetMapping("/company/id/{id}")
    public List<TemplateConfig> findByCompanyId(@PathVariable Long id) {
        return configTemplateService.findByCompanyId(id);
    }

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public File convertDocxToPdf(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        File pdfFile = new File("uploads/" + file.getName() + ".pdf");
        OutputStream out = new FileOutputStream(pdfFile);
        // 1) Load DOCX into XWPFDocument
        XWPFDocument document = new XWPFDocument(is);
        // 2) Prepare Pdf options
        PdfOptions options = PdfOptions.create();
        // 3) Convert XWPFDocument to Pdf
        PdfConverter.getInstance().convert(document, out, options);
        System.out.println(out);
        return pdfFile;
    }

    @GetMapping("/findByCriteria/{value}")
    public List<TemplateConfig> findByCriteria(@PathVariable String value) {
        return fileStorageService.findByCriteria(value);
    }

    @GetMapping("/doctype/{id}")
    public List<TemplateConfig> findByDocTypesIsContaining(@PathVariable Long id) throws Exception {
        return configTemplateService.findByDocTypesIsContaining(id);
    }
}
