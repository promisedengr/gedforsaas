package ma.project.GedforSaas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

import static ma.project.GedforSaas.controller.TranslateController.LOCALE;
import static ma.project.GedforSaas.utils.i18n.Translator.TO_LOCALE;
import static ma.project.GedforSaas.utils.i18n.TranslatorCode.ID_NOT_FOUND;

import ma.project.GedforSaas.model.Company;
import ma.project.GedforSaas.repository.CompanyRepository;
import ma.project.GedforSaas.exception.ResourceNotFoundExceptionConstimized;



@Service
@Transactional
public class CompanyService {
	@Autowired
	private final CompanyRepository companyRepository;

	@Autowired
	public CompanyService(CompanyRepository companyRepository) {
		this.companyRepository = companyRepository;
	}

	public Company addCompany(Company company) {
		return companyRepository.save(company);
	}

	public List<Company> findAllCompanys() {
		return companyRepository.findAll();
	}

	public Company updateCompany(Long id, Company companyRequest) {

		Company companyInDB = companyRepository.getById(id);

		companyInDB.setAdress(companyRequest.getAdress());
		companyInDB.setBusinessNumber(companyRequest.getBusinessNumber());
		companyInDB.setCampagnyName(companyRequest.getCampagnyName());
		companyInDB.setCity(companyRequest.getCity());
		companyInDB.setCountry(companyRequest.getCountry());
		companyInDB.setCreatedBy(companyRequest.getCreatedBy());
		companyInDB.setDateModified(companyRequest.getDateModified());
		companyInDB.setLastModifiedBy(companyRequest.getLastModifiedBy());
		companyInDB.setName(companyRequest.getName());
		companyInDB.setPhoneNumber(companyRequest.getPhoneNumber());
		return companyRepository.save(companyInDB);
	}

	public Company findCompanyById(Long id) {
		return companyRepository.findCompanyById(id)
				.orElseThrow(() -> new ResourceNotFoundExceptionConstimized(TO_LOCALE(ID_NOT_FOUND, LOCALE)));
	}



	public void deleteCompany(Long id) {
		companyRepository.deleteCompanyById(id);
	}

}
