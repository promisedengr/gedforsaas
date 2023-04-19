package ma.project.GedforSaas.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// import ma.project.GedforSaas.model.Company;
import ma.project.GedforSaas.model.Company;
import ma.project.GedforSaas.model.DocType;
import ma.project.GedforSaas.model.TemplateConfig;
import ma.project.GedforSaas.repository.TemplateConfigRepository;


@Service
public class ConfigTemplateService {

	@Autowired
	TemplateConfigRepository templateConfigRepository;
	@Autowired
	DocTypeService docTypeService;

	public TemplateConfig addConfig(TemplateConfig templateConfig) {
		return templateConfigRepository.save(templateConfig);
	}

	public TemplateConfig findById(Long id) {
		return templateConfigRepository.findTemplateConfigById(id);
	}
	//test
	public List<TemplateConfig> findAllTemplate() {
		return templateConfigRepository.findAll();
	}

	@Transactional
	public void deleteById(Long id) {
		templateConfigRepository.deleteById(id);
	}
	public TemplateConfig updateTemplate(Long id, TemplateConfig templateconfig) {

		TemplateConfig tempIndb = templateConfigRepository.getById(id);
		tempIndb.setTitre(templateconfig.getTitre());
		tempIndb.setDescription(templateconfig.getDescription());
		tempIndb.setNature(templateconfig.getNature());
		tempIndb.setRendu(templateconfig.getRendu());
		tempIndb.setDocTypes(templateconfig.getDocTypes());
		tempIndb.setMotor_gen(templateconfig.getMotor_gen());
		tempIndb.setFormat(templateconfig.getFormat());

		//nature , rendu , doctype , doctype_associe, auth_surcharge , doc_cible , motor_gen , format
		return templateConfigRepository.save(tempIndb);
	}

	public List<TemplateConfig> findByDocTypesIsContaining(Long id) throws Exception {
		DocType docType = this.docTypeService.findDoctypeById(id);
		return templateConfigRepository.findByDocTypesIsContaining(docType);
	}

	public List<TemplateConfig> findByCompanyId(Long id) {
		return templateConfigRepository.findByCompanyId(id);
	}
}
