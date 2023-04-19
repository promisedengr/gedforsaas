package ma.project.GedforSaas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ma.project.GedforSaas.model.Document;
import ma.project.GedforSaas.service.DocumentService;

@RestController
@RequestMapping("user/api/v1/document")
public class DocumentUserController {
	@Autowired
	private DocumentService documentService;

	@GetMapping("/company/id/{id}/{trash}")
	public List<Document> findByCompanyId(@PathVariable Long id, @PathVariable boolean trash) {
		return documentService.findByCompanyIdAndTrash(id, trash);
	}

	@GetMapping("/all")
	public ResponseEntity<List<Document>> getAllDocuments() {
		List<Document> documents = documentService.findAllDocuments();

		RestTemplate restTemplate = new RestTemplate();

		String fooResourceUrl = "https://restcountries.com/v3.1/all";

		ResponseEntity<String> response = restTemplate.getForEntity(fooResourceUrl, String.class);

		System.out.println("response : " + response);

		// assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

		return new ResponseEntity<>(documents, HttpStatus.OK);
	}

	@GetMapping("/find/{id}")
	public ResponseEntity<Document> getDocumentById(@PathVariable("id") Long id) {
		Document document = documentService.findDocumentById(id);
		return new ResponseEntity<>(document, HttpStatus.OK);
	}

	@PostMapping("/add")
	public ResponseEntity<Document> addDocument(@RequestBody Document document) {
		Document newDocument = documentService.addDocument(document, document.getFolder());
		return new ResponseEntity<>(newDocument, HttpStatus.CREATED);
	}

	@PutMapping("/update")
	public Document updateDocument(@RequestBody Document document , @RequestParam("doc") String documentString , @RequestParam(value = "files", required = false) MultipartFile [] arrayMultipartFile) {
		return documentService.updateDocument(document,documentString,arrayMultipartFile );
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteDocument(@PathVariable("id") Long id) {
		documentService.deleteDocument(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// TODO: document : find by id to get files of one document
	@GetMapping("/find-content/{id}")
	public ResponseEntity<Object> findDocumentFiles(@PathVariable("id") Long id) {
		Object document = documentService.findDocumentFiles(id);
		return new ResponseEntity<>(document, HttpStatus.OK);
	}

	// TODO: document : find by id to get attributs document
	@GetMapping("/find-attributs/{id}")
	public ResponseEntity<Object> findAttributs(@PathVariable("id") Long id) {
		Object document = documentService.findAttributsDocument(id);
		return new ResponseEntity<>(document, HttpStatus.OK);
	}

	// TODO: document : find by id to get name
	@GetMapping("/find-name/{id}")
	public String getDocumentName(@PathVariable("id") Long id) {
		return documentService.findDocumentToGetName(id);
	}

	// TODO: document : change statut after launch
	@PostMapping("/launch/{id}/{statut}")
	public ResponseEntity<Object> changeStatusDocument(@PathVariable("id") Long id,
			@PathVariable("statut") String statut) {
		Object updateDocumentStatut = documentService.chnageStatut(id, statut);
		return new ResponseEntity<>(updateDocumentStatut, HttpStatus.OK);
	}

	@PostMapping("/trash")
	public Document addDocumentOnTrash(@RequestBody Document document) {
		return documentService.addDocumentOnTrash(document);
	}

}
