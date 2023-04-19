package ma.project.GedforSaas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.project.GedforSaas.model.Permission;
import ma.project.GedforSaas.model.Plan;



public interface PlanRepository extends JpaRepository<Plan, Long> {
    void deletePlanById(Long id);
    List<Plan> findByTypeSubscription(String type);
    Plan findByPriceId(String priceId);
    Plan findByProductId(String productId);
    Optional<Plan> findPlanById(Long id);
    Optional<Plan> findPlanByUserLicence(String email);

}