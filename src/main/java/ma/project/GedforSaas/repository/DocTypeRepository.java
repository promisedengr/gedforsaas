package ma.project.GedforSaas.repository;

import ma.project.GedforSaas.model.DocType;
import ma.project.GedforSaas.model.Template;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocTypeRepository extends JpaRepository<DocType,Long> {
    List<DocType> findByCompanyId(Long id);

    @Query(value = "select * from doctype where id=:id", nativeQuery = true)
    DocType findProprietes(@Param("id") Long id);

    // TODO : get all types of a document
    @Query(value = "select name from doctype", nativeQuery = true)
    List<Object> getTypes();

}
