package ma.project.GedforSaas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.project.GedforSaas.model.Invoice;
import ma.project.GedforSaas.repository.InvoiceRepository;



@Service
public class InvoiceService {
	
	@Autowired
	private InvoiceRepository invoiceRepository;
	
    
    public List<Invoice> saveAll(List<Invoice> invoices){
    	return invoiceRepository.saveAll(invoices);
    }
    
    public List<Invoice> findAll(){
    	return invoiceRepository.findAll();
    }
    
    public Invoice save(Invoice invoice) {
    	return invoiceRepository.save(invoice);
    }
    
    public Optional<Invoice> findById(Long id){
    	return invoiceRepository.findById(id);
    }
    
    
 
}
