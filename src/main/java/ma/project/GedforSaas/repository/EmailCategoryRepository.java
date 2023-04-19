package ma.project.GedforSaas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.project.GedforSaas.model.EmailCategory;

@Repository
public interface EmailCategoryRepository extends JpaRepository<EmailCategory,Long> {
}
