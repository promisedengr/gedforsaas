package ma.project.GedforSaas.service;


import ma.project.GedforSaas.model.FileDB;
import ma.project.GedforSaas.model.Folder;
import ma.project.GedforSaas.repository.FileDBRepository;
// import ma.project.GedforSaas.repository.Filed

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Stream;


@Service
public class FileStorageService {

    @Autowired
    private FileDBRepository fileDBRepository;


    @PreAuthorize("@aclPermissionEvaluator.hasPermission(#folder, 'WRITE')")
    public FileDB store(MultipartFile file, @Param("folder") Folder folder) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        FileDB FileDB = new FileDB(fileName, file.getContentType(), file.getBytes());
        // OcrContent ocrContent = ocrClient.extractTextFromImage(file,"fr");

        return fileDBRepository.save(FileDB);
    }

    public FileDB storeFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        FileDB FileDB = new FileDB(fileName, file.getContentType(), file.getBytes());
        // OcrContent ocrContent = ocrClient.extractTextFromImage(file,"fr");


        return fileDBRepository.save(FileDB);
    }

    public void saveAllFilesList(MultipartFile file) throws IOException {
        // save all files into databases
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        FileDB FileDB = new FileDB(fileName, file.getContentType(), file.getBytes());
        fileDBRepository.save(FileDB);


    }


    public FileDB update(FileDB entity) {
        return fileDBRepository.save(entity);
    }

    public FileDB getFile(String id) {
        return fileDBRepository.findById(id).get();
    }

    public Stream<FileDB> getAllFiles() {
        return fileDBRepository.findAll().stream();
    }
}
