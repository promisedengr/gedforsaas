package ma.project.GedforSaas.controller;

import java.util.List;
import java.util.Optional;

import com.stripe.exception.StripeException;

import ma.project.GedforSaas.model.Invoice;
import ma.project.GedforSaas.model.Subscription;
import ma.project.GedforSaas.repository.InvoiceRepository;
import ma.project.GedforSaas.service.PaymentService;
import ma.project.GedforSaas.service.SubscriptionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin/api/v1/subscription")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/save")
    public Subscription addSubscription(@RequestBody Subscription subscription) {
        return subscriptionService.addSubscription(subscription);
    }

    @GetMapping("/all")
    public List<Subscription> findAllSubscription() {
        return subscriptionService.findAllSubscription();
    }

    @PutMapping("/update")
    public Subscription updateSubscription(@RequestBody Subscription subscription) {
        return subscriptionService.updateSubscription(subscription);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteSubscription(@PathVariable("id") Long id) {
        subscriptionService.deleteSubscription(id);
    }

    @GetMapping("/find/{clientSecret}/{reference}")
    public List<Subscription> findByClientSecretAndReference(@PathVariable("clientSecret") String clientSecret, @PathVariable("reference") String reference) {
        return subscriptionService.findByClientSecretAndReference(clientSecret, reference);
    }


    @GetMapping("/find/user/{id}")
    public Subscription findByUserId(@PathVariable Long id) {
        return subscriptionService.findByUserId(id);
    }


    @GetMapping("/validate/{subId}/{invoiceId}")
    public Subscription validate(@PathVariable Long subId, @PathVariable Long invoiceId) throws Exception {
        Optional<Subscription> subscription = this.subscriptionService.findById(subId);
        Optional<Invoice> invoice = this.invoiceRepository.findById(invoiceId);
        if (subscription.isPresent() && invoice.isPresent()) {
            return paymentService.validate(subscription.get(), invoice.get());
        } else throw new Exception("subscription not found.");
    }

    @PostMapping("/")
    /**
     * Used to create a subscription with strpe checkout page
     * @param checkout
     * @return the subscription id
     * @throws StripeException
     */
    public Subscription subscriptionWithCheckoutPage(@RequestBody Subscription subscription) throws StripeException {
        return this.paymentService.createSubscription(subscription);
    }
}
