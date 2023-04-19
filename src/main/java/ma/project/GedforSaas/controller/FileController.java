package ma.project.GedforSaas.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ma.project.GedforSaas.model.FileDB;
import ma.project.GedforSaas.repository.FileDBRepository;
import ma.project.GedforSaas.response.ResponseFile;
import ma.project.GedforSaas.response.ResponseMessage;
import ma.project.GedforSaas.service.FileStorageService;


@Controller
@CrossOrigin()
public class FileController {

    @Autowired
    private FileStorageService storageService;

    @Autowired
    private FileDBRepository fileDBRepository;

    //private static String  dowloadZip ="download.zip" ;
    private Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    List<String> listOfIds = new ArrayList<>();

    @PostMapping("/uploadSingleFile")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            storageService.storeFile(file);

            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @PostMapping("/uploadMultipleFiles")
    public ResponseEntity<ResponseMessage> uploadFiles(@RequestParam("files") MultipartFile[] files) throws IOException {
        String message = "";
        try {
            List<String> filesNames = new ArrayList<>();

            Arrays.asList(files).stream().forEach(
                    file -> {
                        try {
                            storageService.saveAllFilesList(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        filesNames.add(file.getOriginalFilename());
                    }
            );
            message = "files uploaded successfully";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            e.printStackTrace();
            message = "files didn't added successfully";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));

        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<ResponseFile>> getListFiles() {
        List<ResponseFile> files = storageService.getAllFiles().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files/")
                    .path(dbFile.getId())
                    .toUriString();

            return new ResponseFile(
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getData().length);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }


    @GetMapping("/files/downloadZipFile")
    public void downloadZipFile(HttpServletResponse response) {
        System.out.println(listOfIds.size());
        this.downloadZipFile(response, this.listOfIds);
    }

    @PostMapping("/files/addFileToListZip/")
    void addFileIdToList(@RequestParam("id")  String id) {
        this.listOfIds.add(id);
    }


    @PostMapping("/files/removeFileToListZip/")
    void removeFileIdFromList(@RequestParam("id") String id) {
        this.listOfIds.remove(id);
    }

    @GetMapping("/files/clearListZip/")
    void removeAllFromList() {
        this.listOfIds.clear();
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id) {
        FileDB fileDB = storageService.getFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                .body(fileDB.getData());
    }

    public void downloadZipFile(HttpServletResponse response, List<String> ids) {
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=download.zip");

        try(ZipOutputStream OutputStream = new ZipOutputStream(response.getOutputStream())) {
            for(String id : ids) {
                FileDB file = fileDBRepository.findById(id).get();
                OutputStream.putNextEntry(new ZipEntry(file.getName()));
                OutputStream.write(file.getData());
                OutputStream.closeEntry();
                //InputStream inputStream = new FileInputStream(file.getData().toString());
                // IOUtils.copy(inputStream, OutputStream);
                //StreamUtils.copy(file.getData() ,OutputStream );
            }
            OutputStream.finish();
            OutputStream.close();
            response.setStatus(HttpServletResponse.SC_OK);
            this.removeAllFromList();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
