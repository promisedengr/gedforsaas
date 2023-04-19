package ma.project.GedforSaas.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ma.project.GedforSaas.model.Document;
import ma.project.GedforSaas.model.TemplateConfig;
import ma.project.GedforSaas.service.ConfigTemplateService;
import ma.project.GedforSaas.service.DocumentService;
import ma.project.GedforSaas.utils.WordHelper;

@RestController
@RequestMapping("/api")
public class WordController {
	/*
	 * 
	 * currently on use 
	 */
	Map<String, Object> dataMap = new HashMap<>();

	@Autowired
	DocumentService documentService;
	
	@Autowired
	ConfigTemplateService configTemplateService ;
	
	@GetMapping(value = "/word/{templateName}", produces = "application/vnd.openxmlformats-"
			+ "officedocument.wordprocessingml.document")
	public ResponseEntity<InputStreamResource> word(@PathVariable String templateName) throws IOException, InvalidFormatException {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "inline; filename=mydoc.docx");
//			Document document = documentService.getDocumentById(null);
			TemplateConfig configs = configTemplateService.findById(null);
//			Long id = configs.getId();
//			String parameters = document.getContent();
//			DocUtils.generateWord(dataMap);
			for (Map.Entry<String, Object> entry : dataMap.entrySet()
			) {
				System.out.println("KEY: " + entry.getKey());
				System.out.println("VALUE: " + entry.getValue());
			}
			String mobPath = "uploads/" + templateName;
			ByteArrayInputStream bis = WordHelper.generateWord(dataMap, mobPath);

			return ResponseEntity.ok().headers(headers).body(new InputStreamResource(bis));
		} catch (IOException e) {

			e.printStackTrace();
		}
		return null;

	}
	@PostMapping("/values")
	public Map<String, Object> getDataMap(@RequestBody Map<String, Object> map) throws IOException, InvalidFormatException {
		for (Map.Entry<String, Object> entry : map.entrySet()
		) {
			this.dataMap.put(entry.getKey(), entry.getValue());
		}
		return this.dataMap;
	}

}
//http://localhost:8050/api/word