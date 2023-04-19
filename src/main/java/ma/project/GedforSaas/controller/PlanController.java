package ma.project.GedforSaas.controller;

import java.util.List;

import com.stripe.exception.StripeException;

import ma.project.GedforSaas.model.Plan;
import ma.project.GedforSaas.service.PlanService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/v1/plan")
public class PlanController {

	private final PlanService planService;

	public PlanController(PlanService planService) {
		this.planService = planService;
	}

	@GetMapping("/all")
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
}
