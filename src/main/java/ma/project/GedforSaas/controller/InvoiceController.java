package ma.project.GedforSaas.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ma.project.GedforSaas.model.Invoice;
import ma.project.GedforSaas.service.InvoiceService;

@RestController
@RequestMapping("api/v1/invoice")
public class InvoiceController {
	
	
	@Autowired
	public InvoiceService invoiceService;
	
	@PostMapping("/saveAll")
	public List<Invoice> saveAll(@RequestBody List<Invoice> invoices) {
		return invoiceService.saveAll(invoices);
	}
	@GetMapping("/all")
	public List<Invoice> findAll() {
		return invoiceService.findAll();
	}
	@PostMapping("/save")
	public Invoice save(@RequestBody Invoice invoice) {
		return invoiceService.save(invoice);
	}
	@GetMapping("/all/{id}")
	public Optional<Invoice> findById(@PathVariable Long id) {
		return invoiceService.findById(id);
	}
	
	


	

}
