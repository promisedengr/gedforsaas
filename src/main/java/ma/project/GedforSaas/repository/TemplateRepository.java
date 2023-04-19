package ma.project.GedforSaas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.project.GedforSaas.model.Template;

import java.util.List;

public interface TemplateRepository extends JpaRepository<Template, Long> {
    List<Template> findByCompanyId(Long id);
}