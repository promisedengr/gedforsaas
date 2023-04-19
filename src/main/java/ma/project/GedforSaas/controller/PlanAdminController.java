package ma.project.GedforSaas.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.Gson;
import com.stripe.exception.StripeException;

import ma.project.GedforSaas.model.Plan;
import ma.project.GedforSaas.service.PaymentService;
import ma.project.GedforSaas.service.PlanService;
import ma.project.GedforSaas.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@RestController
@RequestMapping("admin/api/v1/plan")
public class PlanAdminController {


    @Autowired
    private PlanService planService;
    @Autowired
    private PaymentService paymentService;


    private static Gson gson = new Gson();
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ResponseEntity<List<Plan>> getAllPlans() {
        List<Plan> plans = planService.findAllPlans();
        return new ResponseEntity<>(plans, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Plan> getPlanById(@PathVariable("id") Long id) {
        Plan plan = planService.findPlanById(id);
        return new ResponseEntity<>(plan, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Plan> addPlan(@RequestBody Plan plan) throws StripeException {
        Plan newplan = planService.addPlan(plan);
        return new ResponseEntity<>(newplan, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Plan> updatePlan(@RequestBody Plan plan) {
        Plan updatePlan = planService.updatePlan(plan);
        return new ResponseEntity<>(updatePlan, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePlan(@PathVariable("id") Long id) {
        planService.deletePlan(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }




    @GetMapping("/type/{type}")
    public List<Plan> findByTypeSubscription(@PathVariable String type) {
        return planService.findByTypeSubscription(type);
    }

    @GetMapping("/priceId/{priceId}")
    public Plan findByPriceId(@PathVariable String priceId) {
        return planService.findByPriceId(priceId);
    }
    @GetMapping("/productId/{productId}")
    public Plan findByProductId(@PathVariable String productId) {
        return planService.findByProductId(productId);
    }
}
