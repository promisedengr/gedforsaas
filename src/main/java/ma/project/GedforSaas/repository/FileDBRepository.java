package ma.project.GedforSaas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.project.GedforSaas.model.FileDB;


@Repository
public interface FileDBRepository extends JpaRepository<FileDB, String> {

}
