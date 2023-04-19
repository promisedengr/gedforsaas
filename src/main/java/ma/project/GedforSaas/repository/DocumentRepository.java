package ma.project.GedforSaas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Repository;

import java.util.List;
import ma.project.GedforSaas.model.Document;


@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    @PostFilter("@aclPermissionEvaluator.hasPermission(filterObject, 'READ')")
    List<Document> findByCompanyId(Long id);

    List<Document> findAllByCompanyId(Long id);

    @PostFilter("@aclPermissionEvaluator.hasPermission(filterObject, 'READ')")
    List<Document> findByCompanyIdAndTrash(Long id, boolean trash);

    @PostFilter("@aclPermissionEvaluator.hasPermission(filterObject, 'READ')")
    List<Document> findByCompanyIdAndFolderId(Long companyId, Long folderId);

    @PostFilter("@aclPermissionEvaluator.hasPermission(filterObject, 'READ')")
    List<Document> findByFolderId(Long folderId);


    List<Document> findAllByFolderId(Long folderId);

    @PostFilter("@aclPermissionEvaluator.hasPermission(filterObject, 'READ')")
    List<Document> findByCompanyIdAndLabelId(Long companyId, Long labelId);

    // get attributs
    @Query(value = "SELECT * FROM document where document_id=:id", nativeQuery = true)
    Document findAttributs(@Param("id") Long id);

    // get content
    @Query(value = "SELECT * FROM document where document_id=:id", nativeQuery = true)
    Document findDocumentContent(@Param("id") Long id);

    // get name
    @Query(value = "SELECT name FROM document where document_id=:id", nativeQuery = true)
    String findByIdToGetName(@Param("id") Long id);


//    @PostFilter("hasPermission(filterObject, 'READ')")
//    List<Document> findAll();
//
//    @PostAuthorize("hasPermission(#id,'Document', 'READ')")
//    Optional<Document> findById(Long id);


}
