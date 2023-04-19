package ma.project.GedforSaas.controller;

import freemarker.template.TemplateException;
import ma.project.GedforSaas.model.Resource;
import ma.project.GedforSaas.service.ResourceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/resource")
public class ResourceController {

	@Autowired
	private ResourceService resourceService;

	@GetMapping("/company/id/{id}")
	public List<Resource> findByCompanyId(@PathVariable Long id) {
		return resourceService.findByCompanyId(id);
	}

	@GetMapping("/trash/company/id/{id}")
	public List<Resource> findByTrashResources(@PathVariable Long id) {
		return resourceService.findByTrashResources(id);
	}

	@PostMapping("/add")
	public ResponseEntity<Resource> addResource(@RequestBody Resource resource) throws Exception {
		return new ResponseEntity<>(resourceService.addResource(resource), HttpStatus.CREATED);
	}

	@GetMapping("/all")
	public ResponseEntity<List<Resource>> getAllResource() {
		return new ResponseEntity<>(resourceService.findAllResources(), HttpStatus.OK);
	}

	@GetMapping("/find/{id}")
	public ResponseEntity<Resource> getResourceById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(resourceService.findResourceById(id), HttpStatus.OK);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Resource> updateResource(@PathVariable("id") Long id, @RequestBody Resource resource) {
		return new ResponseEntity<>(resourceService.updateResource(id, resource), HttpStatus.OK);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteResource(@PathVariable("id") Long id) {
		resourceService.deleteResource(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping("/changeState/{id}")
	public ResponseEntity<Resource> changeState(@PathVariable("id") Long id, @RequestBody Resource resource) {
		return new ResponseEntity<>(resourceService.changeState(id, resource.isActive()), HttpStatus.OK);
	}

	@PostMapping("/sendMail")
	public ResponseEntity<Object> sendMail(@RequestBody Resource resource)
			throws IOException, TemplateException, MessagingException {
		return new ResponseEntity<>(resourceService.sendMail(resource), HttpStatus.CREATED);
	}

	@PostMapping("/trash")
	public Resource addResourceOnTrash(@RequestBody Resource resource) {
		return resourceService.addResourceOnTrash(resource);
	}

	@GetMapping("/findByCriteria/{companyId}/{resource}")
	public List<Resource> findByCriteria(@PathVariable String resource,@PathVariable Long companyId) {
		return resourceService.findByCriteria(resource, companyId);
	}
}
