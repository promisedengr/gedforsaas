package ma.project.GedforSaas.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ma.project.GedforSaas.model.Company;


@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    void deleteCompanyById(Long id);

    Optional<Company> findCompanyById(Long id);
}