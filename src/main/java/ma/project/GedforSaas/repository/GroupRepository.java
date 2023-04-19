package ma.project.GedforSaas.repository;

import java.util.List;
import java.util.Optional;

import ma.project.GedforSaas.model.Folder;
import ma.project.GedforSaas.model.Group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNullApi;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByName(String name);
    List<Group> findByCompanyId(Long id);
//
//
//
//
//    @PreAuthorize("hasPermission(#group, 'WRITE')")
//    Group save(@Param("group")Group group);

}