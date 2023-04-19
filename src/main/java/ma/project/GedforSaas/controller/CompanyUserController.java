package ma.project.GedforSaas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ma.project.GedforSaas.model.Company;
import ma.project.GedforSaas.service.CompanyService;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("user/api/v1/company")
public class CompanyUserController {
	@Autowired
	private CompanyService companyService;

	@GetMapping("/all")
	public ResponseEntity<List<Company>> getAllCompanys() {
		List<Company> companys = companyService.findAllCompanys();
		return new ResponseEntity<>(companys, HttpStatus.OK);
	}

	@GetMapping("/find/{id}")
	public ResponseEntity<Company> getCompanyById(@PathVariable("id") Long id) {
		Company company = companyService.findCompanyById(id);
		return new ResponseEntity<>(company, HttpStatus.OK);
	}

	@PostMapping("/add")
	public ResponseEntity<Company> addCompany(@RequestBody Company company) {
		Company newCompany = companyService.addCompany(company);
		return new ResponseEntity<>(newCompany, HttpStatus.CREATED);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Company> updateUser(@PathVariable("id") Long id, @RequestBody Company company) {
		Company updateCompany = companyService.updateCompany(id, company);
		return new ResponseEntity<>(updateCompany, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteCompany(@PathVariable("id") Long id) {
		companyService.deleteCompany(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
