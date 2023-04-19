package ma.project.GedforSaas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.project.GedforSaas.model.Group;
import ma.project.GedforSaas.model.Permission;



public interface PermissionRepository extends JpaRepository<Permission, Long> {
    void deletePermissionById(Long id);

    Optional<Permission> findPermissionById(Long id);
    List<Permission> findByCompanyId(Long id);
}