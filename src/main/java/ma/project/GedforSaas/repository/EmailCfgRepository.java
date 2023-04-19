package ma.project.GedforSaas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.project.GedforSaas.exception.EmailCfg;

@Repository
public interface EmailCfgRepository  extends JpaRepository<EmailCfg, Long> {
    EmailCfg findByCompanyId(Long id);
    boolean existsByCompanyId(Long id);
}
