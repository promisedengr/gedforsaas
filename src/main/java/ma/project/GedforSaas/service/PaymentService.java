package ma.project.GedforSaas.service;

import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.Subscription;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.SubscriptionCreateParams;

import ma.project.GedforSaas.model.Invoice;
import ma.project.GedforSaas.model.Plan;
import ma.project.GedforSaas.model.User;
import ma.project.GedforSaas.repository.InvoiceRepository;
import ma.project.GedforSaas.repository.PlanRepository;
import ma.project.GedforSaas.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PaymentService {
    private static Gson gson = new Gson();
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;

    private static void init() {
        Stripe.apiKey = "sk_test_51LeiCsIa4mjYLXpfAjwvBCJDtDj36aeWTt3MHkvmTgw5TLzafPWk6VKBgMZpPiDTHml6n2DEcxEVc1iOrPPZlHQQ00wS8CemXD";
    }

    public String createPaymentIntent() throws StripeException {
        init();
        List<Object> paymentMethodTypes =
                new ArrayList<>();
        paymentMethodTypes.add("card");
        Map<String, Object> params = new HashMap<>();
        params.put("amount", 100);
        params.put("currency", "usd");
        params.put(
                "payment_method_types",
                paymentMethodTypes
        );

        PaymentIntent paymentIntent =
                PaymentIntent.create(params);
        return gson.toJson(paymentIntent);
    }


    /**
     * create customer in stripe account
     *
     * @param user
     * @return User
     * @throws StripeException
     */
    public User createCustomer(User user) throws StripeException {
        init();
        CustomerCreateParams params =
                CustomerCreateParams
                        .builder()
                        .setEmail(user.getEmail())
                        .setName(user.getFirstName() + " " + user.getLastName())
                        .setShipping(
                                CustomerCreateParams.Shipping
                                        .builder()
                                        .setAddress(
                                                CustomerCreateParams.Shipping.Address
                                                        .builder()
                                                        .setCity(user.getCity())
                                                        .setCountry(user.getCountry())
                                                        .setLine1(user.getCity())
                                                        .setPostalCode(user.getPostalCode())
                                                        .setState(" ")
                                                        .build()
                                        )
                                        .setName(user.getFirstName() + "_" + user.getLastName())
                                        .build()
                        )
                        .setAddress(
                                CustomerCreateParams.Address
                                        .builder()
                                        .setCity(user.getCity())
                                        .setCountry(user.getCountry())
                                        .setLine1(user.getAddress())
                                        .setPostalCode(user.getPostalCode())
                                        .setState(" ")
                                        .build()
                        )
                        .build();

        Customer customer = Customer.create(params);
        user.setCustomerId(customer.getId());
        return this.userRepository.save(user);
    }


    /**
     * Create subscription for customer
     *
     * @return clientSecret AND subscriptionID
     * @throws StripeException
     */

    public ma.project.GedforSaas.model.Subscription createSubscription(ma.project.GedforSaas.model.Subscription mySubscription) throws StripeException {
        init();
        // Automatically save the payment method to the subscription
        // when the first payment is successful.
        SubscriptionCreateParams.PaymentSettings paymentSettings =
                SubscriptionCreateParams.PaymentSettings
                        .builder()
                        .setSaveDefaultPaymentMethod(SubscriptionCreateParams.PaymentSettings.SaveDefaultPaymentMethod.ON_SUBSCRIPTION)
                        .build();

        // Create the subscription. Note we're expanding the Subscription's
        // latest invoice and that invoice's payment_intent
        // so we can pass it to the front end to confirm the payment
        SubscriptionCreateParams subCreateParams = SubscriptionCreateParams
                .builder()
                .setCustomer(mySubscription.getUser().getCustomerId())
                .addItem(
                        SubscriptionCreateParams
                                .Item.builder()
                                .setPrice(mySubscription.getPlan().getPriceId())
                                .build()
                )
                .setPaymentSettings(paymentSettings)
                .setPaymentBehavior(SubscriptionCreateParams.PaymentBehavior.DEFAULT_INCOMPLETE)
                .addAllExpand(Arrays.asList("latest_invoice.payment_intent"))
                .build();

        //create subscription
        com.stripe.model.Subscription subscription = Subscription.create(subCreateParams);
        cloneSubscription(mySubscription, subscription);
        ma.project.GedforSaas.model.Subscription s = this.subscriptionService.addSubscription(mySubscription);

        //create invoice
        ma.project.GedforSaas.model.Invoice invoice = new Invoice();
        createInvoice(subscription, s, invoice);
        List<Invoice> invoices = new ArrayList<>();
        invoices.add(invoice);
        s.setInvoices(invoices);
        return s;
    }

    private Invoice createInvoice(Subscription stripeSubscription, ma.project.GedforSaas.model.Subscription subscription1, Invoice invoice) {
        invoice.setStatus(stripeSubscription.getLatestInvoiceObject().getStatus());
        invoice.setPeriodEnd(subscription1.getPeriodEnd());
        invoice.setPeriodStart(subscription1.getPeriodStart());
        invoice.setAmount(subscription1.getPlan().getAmount());
        invoice.setInvoiceNumber(stripeSubscription.getLatestInvoiceObject().getId());
        invoice.setCurrency(stripeSubscription.getLatestInvoiceObject().getCurrency());
        invoice.setPaid(stripeSubscription.getLatestInvoiceObject().getPaid());
        invoice.setSubscription(subscription1);
        return this.invoiceRepository.save(invoice);
    }

    private ma.project.GedforSaas.model.Subscription cloneSubscription(ma.project.GedforSaas.model.Subscription mySubscription, Subscription stripeSubscription) {
        mySubscription.setClientSecret(stripeSubscription.getLatestInvoiceObject().getPaymentIntentObject().getClientSecret());
        mySubscription.setReference(stripeSubscription.getId());
        mySubscription.setStatus(stripeSubscription.getStatus());
        mySubscription.setNextInvoices(stripeSubscription.getLatestInvoice());
        mySubscription.setCurrentPeriod(stripeSubscription.getLatestInvoice());
        return mySubscription;
    }

    /**
     * create product in stripe
     *
     * @return Plan
     * @throws StripeException
     */
    public ma.project.GedforSaas.model.Plan createPlan(ma.project.GedforSaas.model.Plan plan) throws StripeException {
        init();
        double amountd = Double.parseDouble(plan.getAmount()) * 100;
        Integer amount = (int) amountd;
        //create product
        Map<String, Object> productParams = new HashMap<>();
        productParams.put("name", plan.getTitle());
        productParams.put("description", plan.getDescription());
        Product product = Product.create(productParams);
        //create price
        Map<String, Object> recurring = new HashMap<>();
        recurring.put("interval", plan.getTypeSubscription());
        Map<String, Object> priceParams = new HashMap<>();
        priceParams.put("unit_amount", amount);
        priceParams.put("currency", plan.getCurrency().toLowerCase());
        priceParams.put("recurring", recurring);
        priceParams.put("product", product.getId());
        Price price = Price.create(priceParams);
        plan.setPriceId(price.getId());
        plan.setProductId(product.getId());
        return planRepository.save(plan);
    }

    public void updatePlan(Plan plan) throws StripeException {
        init();
        double amountd = Double.parseDouble(plan.getAmount()) * 100;
        Integer amount = (int) amountd;

        //update product
        Product deletedProduct =
                Product.retrieve(plan.getProductId());
        Map<String, Object> params = new HashMap<>();
        params.put("name", plan.getTitle());
        params.put("description", plan.getDescription());
        Product p = deletedProduct.update(params);

        //archive price
        Price updatedPrice = Price.retrieve(plan.getPriceId());
        updatedPrice.setActive(false);
        updatedPrice.setDeleted(true);
        //create new  price
        Map<String, Object> recurring = new HashMap<>();
        recurring.put("interval", plan.getTypeSubscription());
        Map<String, Object> priceParams = new HashMap<>();
        priceParams.put("unit_amount", amount);
        priceParams.put("currency", plan.getCurrency().toLowerCase());
        priceParams.put("recurring", recurring);
        priceParams.put("product", p.getId());
        Price.create(priceParams);
    }


    public ma.project.GedforSaas.model.Subscription validate(ma.project.GedforSaas.model.Subscription subscription, Invoice invoice) throws StripeException {
        init();
        com.stripe.model.Subscription stripeSubscription = Subscription.retrieve(subscription.getReference());
        subscription.setStatus(stripeSubscription.getStatus()); //active
        ma.project.GedforSaas.model.Subscription s = this.subscriptionService.addSubscription(subscription);

        //update invoice
        Date dateNow = new Date();

        invoice.setPaid(true);
        invoice.setStatus("paid");
//        invoice.setDatePayment(stripeSubscription.getLatestInvoiceObject().getPaymentIntentObject().);
        invoiceRepository.save(invoice);
        List<Invoice> invoices = new ArrayList<>();
        invoices.add(invoice);
        s.setInvoices(invoices);
        return s;
    }
}
