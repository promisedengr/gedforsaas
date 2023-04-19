package ma.project.GedforSaas.service;

import com.stripe.exception.StripeException;
import ma.project.GedforSaas.exception.ResourceNotFoundExceptionConstimized;
import ma.project.GedforSaas.model.Plan;
import ma.project.GedforSaas.repository.PlanRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static ma.project.GedforSaas.controller.TranslateController.LOCALE;
import static ma.project.GedforSaas.utils.i18n.Translator.TO_LOCALE;
import static ma.project.GedforSaas.utils.i18n.TranslatorCode.ID_NOT_FOUND;

@Service
@Transactional
public class PlanService {
    private final PlanRepository planRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    public PlanService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public Plan addPlan(Plan plan) throws StripeException {
        Long ID = plan.getId();
        Plan p = planRepository.save(plan);
        if (ID == null) {
            System.out.println("ID: " + ID);
            return paymentService.createPlan(p);
        } else {
            System.out.println("ID: " + ID);
            paymentService.updatePlan(p);
            return p;
        }
    }

    public List<Plan> findByTypeSubscription(String type) {
        return planRepository.findByTypeSubscription(type);
    }

    public Plan findByPriceId(String priceId) {
        return planRepository.findByPriceId(priceId);
    }

    public Plan findByProductId(String productId) {
        return planRepository.findByProductId(productId);
    }

    public List<Plan> findAllPlans() {
        return planRepository.findAll();
    }

    public Plan updatePlan(Plan plan) {
        return planRepository.save(plan);
    }

    public Plan findPlanById(Long id) {
        return planRepository.findPlanById(id)
                .orElseThrow(() -> new ResourceNotFoundExceptionConstimized(TO_LOCALE(ID_NOT_FOUND, LOCALE)));
    }

    public Optional<Plan> findPlanByUserLicence(String email) {
        return planRepository.findPlanByUserLicence(email);
    }

    public void deletePlan(Long id) {
        planRepository.deletePlanById(id);
    }

}
