package ma.project.GedforSaas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.project.GedforSaas.model.DocType;
import ma.project.GedforSaas.model.TemplateConfig;

import java.util.List;

@Repository
public interface TemplateConfigRepository extends  JpaRepository<TemplateConfig, Long> {

	TemplateConfig findTemplateConfigById(Long id);
	List<TemplateConfig> findByCompanyId(Long id);
	List<TemplateConfig> findByDocTypesIsContaining(DocType docType);
}
