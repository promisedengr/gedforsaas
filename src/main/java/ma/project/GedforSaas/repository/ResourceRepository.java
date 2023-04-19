package ma.project.GedforSaas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.project.GedforSaas.model.Permission;
import ma.project.GedforSaas.model.Resource;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

	Optional<Resource> findByPersonalEmail(String personnalEmail);
	List<Resource> findByCompanyIdAndTrash(Long id, boolean trash);

}