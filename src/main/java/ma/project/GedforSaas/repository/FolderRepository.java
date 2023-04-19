package ma.project.GedforSaas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.project.GedforSaas.model.Folder;

import java.util.List;
import java.util.Optional;


public interface FolderRepository extends JpaRepository<Folder, Long> {


    Folder findByName(String name);

    Optional<Folder> findFolderById(Long id);


    List<Folder> findByCompanyIdAndTrashAndParentFolderId(Long id,boolean trash, Long folderId);
    List<Folder> findByCompanyId(Long id);

    List<Folder> findByFolderId(Long id);
    List<Folder> findByDocTypeId(Long id);

}