package ma.project.GedforSaas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.project.GedforSaas.model.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

}
