package ma.project.GedforSaas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Repository;

import ma.project.GedforSaas.model.Call;

import java.util.List;
import java.util.Optional;

@Repository
public interface CallRepository extends JpaRepository<Call, Long> {
	
	Optional<Call> findById(Long id);

	List<Call> findByCompanyId(Long id);
	Call findByTitle (String title);
	int deleteByTitle(String title);
	List<Call> findByResourceId(Long id);
	

}
