package ma.project.GedforSaas.service;

import ma.project.GedforSaas.abstractSupperClasses.AbstractService;
import ma.project.GedforSaas.model.Call;
// import ma.project.GedforSaas.model.Company;
import ma.project.GedforSaas.model.Company;
import ma.project.GedforSaas.model.Note;
import ma.project.GedforSaas.model.Resource;
import ma.project.GedforSaas.repository.CallRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ma.project.GedforSaas.controller.TranslateController.LOCALE;
import static ma.project.GedforSaas.utils.i18n.Translator.TO_LOCALE;
import static ma.project.GedforSaas.utils.i18n.TranslatorCode.COMPANY_NOT_FOUND;

@Service
public class CallService {

	private List<Call> callList = new ArrayList<>();

	public List<Call> findByCompanyId(Long id) {
		return callRepository.findByCompanyId(id);
	}

	@Autowired
	private CallRepository callRepository;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private EntityManager entityManager;
	@Transactional
	public int deleteByTitle(String title) {
		return callRepository.deleteByTitle(title);
	}

	@Transactional
	public void deleteById(Long id) {
		callRepository.deleteById(id);
	}

	public Optional<Call> findById(Long id) {
		return callRepository.findById(id);
	}

	public Call findByTitle(String title) {
		return callRepository.findByTitle(title);
	}

	//save
	public Call save(Call call) throws Exception {
		Company company = companyService.findCompanyById(call.getCompany().getId());
		if (call.getResource().getId() != 0){
			Resource resource = resourceService.getResourceById(call.getResource().getId());
			call.setResource(resource);
		} else {
			call.setResource(null);
		}

		if (company == null) {
			throw new Exception(TO_LOCALE(COMPANY_NOT_FOUND,LOCALE));
		} else {
			call.setCompany(company);
		}

		Call call1 = callRepository.save(call);
		this.callList=this.findAll();
		return call1;
	}

	public List<Call> findAll() {
		return callRepository.findAll();
	}
	
	//update
	public Call update(Call call)
	{  
		return callRepository.save(call);
	}

	public List<Call> findByResourceId(Long id) {
		return callRepository.findByResourceId(id);
	}

	public List<Call> findByCriteria(String text, Long id) {
		String query = AbstractService.init(id, "Call"); // here we add table name be careful to make first letter Uppercase
		if (text != null) {
			query += AbstractService.addConstraint("o", "title", text, "LIKE", "  ");
			query += AbstractService.addConstraint("o", "details", text, "LIKE", "OR");
			query += AbstractService.addConstraint("o", "resource.firstName", text, "LIKE", "OR");
			query += AbstractService.addConstraint("o", "resource.lastName", text, "LIKE", "OR");
			query += AbstractService.addConstraint("o", "resource.personalEmail", text, "LIKE", "OR");
			query += AbstractService.addConstraint("o", "resource.professionalEmail", text, "LIKE", "OR");
			query += AbstractService.addConstraint("o", "resourceCalled", text, "LIKE", "OR");
		}
		System.out.println(query);
		return entityManager.createQuery(query).getResultList();
	}
}
