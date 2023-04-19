package ma.project.GedforSaas.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ma.project.GedforSaas.model.Employee;
import ma.project.GedforSaas.service.ConfigTemplateService;
import ma.project.GedforSaas.service.DocumentService;

@RestController
@RequestMapping("/api")
public class ZExcelController {

	private static final Logger logger = LoggerFactory.getLogger(ZExcelController.class);

	Map<String, Object> dataMap = new HashMap<>();

	@Autowired
	DocumentService documentService;

	@Autowired
	ConfigTemplateService configTemplateService;

	@GetMapping(value = "/excel", produces = "application/octet-stream")
	public void createDocument(OutputStream outStream) {
		HttpHeaders headers = new HttpHeaders();
		// headers.add("Content-Disposition", "inline; filename=render.xls");
		logger.debug("Start creation of document");

		String pathTemplateName = ("uploads/9-template_excel.xls");
		try (InputStream input = new FileInputStream(pathTemplateName)) {
			// this.getClass().getResourceAsStream(pathTemplateName)) {// 1
			Map<String, Object> staticMap = new HashMap<>();
			Employee employee = new Employee("test", "test2", "test3", "test4");
			staticMap.put("employee.name", employee.getName());
			staticMap.put("employee.bithdate", employee.getBirthdate());
			staticMap.put("employee.payment", employee.getPayment());
			staticMap.put("employee.bonus", employee.getBonus());
			System.out.println(employee);
			Context context = new Context();
			for (Entry<String, Object> element : staticMap.entrySet()) { // 2
				context.putVar(element.getKey(), element.getValue());
			}

			JxlsHelper.getInstance().processTemplate(input, outStream, context); // 3*

		} catch (

		Exception exception) {
			logger.error("Fail to generate the document", exception);
		} finally {
			closeAndFlushOutput(outStream); // 4
		}
	}

//	@PostMapping("/exelvalues")
//	public Map<String, Object> getDataMap(@RequestBody Map<String, Object> map) throws IOException, InvalidFormatException {
//		for (Map.Entry<String, Object> entry : map.entrySet()
//		) {
//			this.dataMap.put(entry.getKey(), entry.getValue());
//		}
//		return this.dataMap;
//	}

	private void closeAndFlushOutput(OutputStream outStream) {
		try {
			outStream.flush();
			outStream.close();
		} catch (IOException exception) {
			logger.error("Fail to flush and close the output", exception);
		}
	}

}
