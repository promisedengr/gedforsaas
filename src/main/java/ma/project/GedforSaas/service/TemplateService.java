package ma.project.GedforSaas.service;

import ma.project.GedforSaas.model.Company;
import ma.project.GedforSaas.model.Template;
import ma.project.GedforSaas.repository.TemplateRepository;
import ma.project.GedforSaas.exception.ResourceNotFoundExceptionConstimized;
import ma.project.GedforSaas.abstractSupperClasses.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

import static ma.project.GedforSaas.controller.TranslateController.LOCALE;
import static ma.project.GedforSaas.utils.i18n.Translator.TO_LOCALE;
import static ma.project.GedforSaas.utils.i18n.TranslatorCode.*;

@Service
@Transactional
public class TemplateService {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private DocTypeService doctypeService;
    @Autowired
    private CompanyService companyService;

    public List<Template> findByCompanyId(Long id) {
        return templateRepository.findByCompanyId(id);
    }

    public Template addTemplate(Template template) throws Exception {
        Company company = companyService.findCompanyById(template.getCompany().getId());
        if (company == null) {
            throw new Exception(TO_LOCALE(COMPANY_NOT_FOUND,LOCALE));
        } else {
            template.setCompany(company);
        }
        return templateRepository.save(template);
    }

    public List<Template> findAllTemplates() {
        return templateRepository.findAll();
    }

    public Template updateTemplate(Long id, Template template) {

        // TODO: get the template from database by id
        Template templateFromDB = templateRepository.getOne(id);

        // check if the object exist or not
        if (templateFromDB == null) {
            throw new ResourceNotFoundExceptionConstimized(TO_LOCALE(TEMPLATE_NOT_FOUND, LOCALE));
        }

        // check if the template is a default one
        if (templateFromDB.isDefaultType()) {
            // if it's a default template create a new one
            Template newTemplate = new Template(template.getName(),  template.getContentJson());
            return templateRepository.save(newTemplate);
        }

        // if its not a default one then update its fields
        templateFromDB.setContentJson(template.getContentJson());
        templateFromDB.setDescription(template.getDescription());
//        templateFromDB.setDocuments(template.getDocuments());
        templateFromDB.setName(template.getName());

        // save in the old instance
        return templateRepository.save(templateFromDB);
    }

    public Template findTemplateById(Long id) {
        return templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExceptionConstimized(TO_LOCALE(ID_NOT_FOUND, LOCALE)));
    }

    public void deleteTemplate(Long id) {
        templateRepository.deleteById(id);
    }


    public List<Template> findByCriteria(String value,Long id) {
        String query = AbstractService.init(id, "Template"); // here we add table name be careful to make first letter Uppercase
        query += AbstractService.addConstraint("o", "name", value, "LIKE", "");
        query += AbstractService.addConstraint("o", "description", value, "LIKE", "OR");
        query += AbstractService.addConstraint("o", "contentJson", value, "LIKE", "OR");
        System.out.println(query);
        return entityManager.createQuery(query).getResultList();
    }


}
