package ma.project.GedforSaas.model;

// import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String amount;
	private String invoiceNumber; 
	private String datePayment;
	private String dueDate;
	private String status;
	private boolean paid;
	private String currency;
	@JsonFormat(pattern = "MM dd yyyy hh:mm")
	private String periodStart;
	@JsonFormat(pattern = "MM dd yyyy hh:mm")
	private String periodEnd;
	@ManyToOne(fetch = FetchType.EAGER)
	@JsonIdentityReference(alwaysAsId = true)
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
	private Subscription subscription;
}
/**
 * {
 *     "automatic_tax": {
 *         "enabled": false
 *     },
 *     "billing_cycle_anchor": 1662724980,
 *     "cancel_at_period_end": false,
 *     "collection_method": "charge_automatically",
 *     "created": 1662724980,
 *     "currency": "usd",
 *     "current_period_end": 1694260980,
 *     "current_period_start": 1662724980,
 *     "customer": {
 *         "id": "cus_MOtfN6d4Ouavi0"
 *     },
 *     "default_tax_rates": [],
 *     "id": "sub_1Lg5vEIa4mjYLXpfdRlYQ7Bi",
 *     "items": {
 *         "object": "list",
 *         "data": [
 *             {
 *                 "created": 1662724981,
 *                 "id": "si_MOtihSvSyzYjep",
 *                 "metadata": {},
 *                 "object": "subscription_item",
 *                 "plan": {
 *                     "active": true,
 *                     "amount": 1500,
 *                     "amount_decimal": 1500,
 *                     "billing_scheme": "per_unit",
 *                     "created": 1662653906,
 *                     "currency": "usd",
 *                     "id": "price_1LfnQsIa4mjYLXpf4lRMYqS0",
 *                     "interval": "year",
 *                     "interval_count": 1,
 *                     "livemode": false,
 *                     "metadata": {},
 *                     "object": "plan",
 *                     "product": {
 *                         "id": "prod_MOackDStVG2l0e"
 *                     },
 *                     "usage_type": "licensed"
 *                 },
 *                 "price": {
 *                     "active": true,
 *                     "billing_scheme": "per_unit",
 *                     "created": 1662653906,
 *                     "currency": "usd",
 *                     "id": "price_1LfnQsIa4mjYLXpf4lRMYqS0",
 *                     "livemode": false,
 *                     "metadata": {},
 *                     "object": "price",
 *                     "product": {
 *                         "id": "prod_MOackDStVG2l0e"
 *                     },
 *                     "recurring": {
 *                         "interval": "year",
 *                         "interval_count": 1,
 *                         "usage_type": "licensed"
 *                     },
 *                     "tax_behavior": "unspecified",
 *                     "type": "recurring",
 *                     "unit_amount": 1500,
 *                     "unit_amount_decimal": 1500
 *                 },
 *                 "quantity": 1,
 *                 "subscription": "sub_1Lg5vEIa4mjYLXpfdRlYQ7Bi",
 *                 "tax_rates": []
 *             }
 *         ],
 *         "hasMore": false,
 *         "url": "/v1/subscription_items?subscription=sub_1Lg5vEIa4mjYLXpfdRlYQ7Bi"
 *     },
 */

 /**     "latest_invoice": {
 *         "id": "in_1Lg5vEIa4mjYLXpfEmqZsTRc",
 *         "expandedObject": {
 *             "account_country": "US",
 *             "account_name": "Elmoudene",
 *             "amount_due": 1500,
 *             "amount_paid": 0,
 *             "amount_remaining": 1500,
 *             "attempt_count": 0,
 *             "attempted": false,
 *             "auto_advance": false,
 *             "automatic_tax": {
 *                 "enabled": false
 *             },
 *             "billing_reason": "subscription_create",
 *             "collection_method": "charge_automatically",
 *             "created": 1662724980,
 *             "currency": "usd",
 *             "customer": {
 *                 "id": "cus_MOtfN6d4Ouavi0"
 *             },
 *             "customer_address": {
 *                 "state": " "
 *             },
 *             "customer_email": "youssefelmoudene08@gmail.com",
 *             "customer_name": "YOUSSEF EL MOUDENE",
 *             "customer_shipping": {
 *                 "address": {
 *                     "state": " "
 *                 },
 *                 "name": "YOUSSEF_EL MOUDENE"
 *             },
 *             "customer_tax_exempt": "none",
 *             "customer_tax_ids": [],
 *             "default_tax_rates": [],
 *             "discounts": [],
 *             "ending_balance": 0,
 *             "hosted_invoice_url": "https://invoice.stripe.com/i/acct_1LeiCsIa4mjYLXpf/test_YWNjdF8xTGVpQ3NJYTRtallMWHBmLF9NT3RpcDBqWlE4U2VuSXNoa21OYWVnMFJtSW43WTJuLDUzMjY1Nzgx0200WEKSVyTH?s=ap",
 *             "id": "in_1Lg5vEIa4mjYLXpfEmqZsTRc",
 *             "invoice_pdf": "https://pay.stripe.com/invoice/acct_1LeiCsIa4mjYLXpf/test_YWNjdF8xTGVpQ3NJYTRtallMWHBmLF9NT3RpcDBqWlE4U2VuSXNoa21OYWVnMFJtSW43WTJuLDUzMjY1Nzgx0200WEKSVyTH/pdf?s=ap",
 *             "lines": {
 *                 "object": "list",
 *                 "data": [
 *                     {
 *                         "amount": 1500,
 *                         "amount_excluding_tax": 1500,
 *                         "currency": "usd",
 *                         "description": "1 × Small Entreprise (at $15.00 / year)",
 *                         "discountable": true,
 *                         "discount_amounts": [],
 *                         "discounts": [],
 *                         "id": "il_1Lg5vEIa4mjYLXpfmpmwQRHe",
 *                         "livemode": false,
 *                         "metadata": {},
 *                         "object": "line_item",
 *                         "period": {
 *                             "end": 1694260980,
 *                             "start": 1662724980
 *                         },
 *                         "plan": {
 *                             "active": true,
 *                             "amount": 1500,
 *                             "amount_decimal": 1500,
 *                             "billing_scheme": "per_unit",
 *                             "created": 1662653906,
 *                             "currency": "usd",
 *                             "id": "price_1LfnQsIa4mjYLXpf4lRMYqS0",
 *                             "interval": "year",
 *                             "interval_count": 1,
 *                             "livemode": false,
 *                             "metadata": {},
 *                             "object": "plan",
 *                             "product": {
 *                                 "id": "prod_MOackDStVG2l0e"
 *                             },
 *                             "usage_type": "licensed"
 *                         },
 *                         "price": {
 *                             "active": true,
 *                             "billing_scheme": "per_unit",
 *                             "created": 1662653906,
 *                             "currency": "usd",
 *                             "id": "price_1LfnQsIa4mjYLXpf4lRMYqS0",
 *                             "livemode": false,
 *                             "metadata": {},
 *                             "object": "price",
 *                             "product": {
 *                                 "id": "prod_MOackDStVG2l0e"
 *                             },
 *                             "recurring": {
 *                                 "interval": "year",
 *                                 "interval_count": 1,
 *                                 "usage_type": "licensed"
 *                             },
 *                             "tax_behavior": "unspecified",
 *                             "type": "recurring",
 *                             "unit_amount": 1500,
 *                             "unit_amount_decimal": 1500
 *                         },
 *                         "proration": false,
 *                         "proration_details": {},
 *                         "quantity": 1,
 *                         "subscription": "sub_1Lg5vEIa4mjYLXpfdRlYQ7Bi",
 *                         "subscription_item": "si_MOtihSvSyzYjep",
 *                         "tax_amounts": [],
 *                         "tax_rates": [],
 *                         "type": "subscription",
 *                         "unit_amount_excluding_tax": 1500
 *                     }
 *                 ],
 *                 "hasMore": false,
 *                 "url": "/v1/invoices/in_1Lg5vEIa4mjYLXpfEmqZsTRc/lines"
 *             },
 *             "livemode": false,
 *             "metadata": {},
 *             "number": "B81AC55C-0001",
 *             "object": "invoice",
 *             "paid": false,
 *             "paid_out_of_band": false,
 *             "payment_intent": {
 *                 "id": "pi_3Lg5vEIa4mjYLXpf1dpSlpx5",
 *                 "expandedObject": {
 *                     "amount": 1500,
 *                     "amount_capturable": 0,
 *                     "amount_details": {
 *                         "tip": {}
 *                     },
 *                     "amount_received": 0,
 *                     "capture_method": "automatic",
 *                     "charges": {
 *                         "object": "list",
 *                         "data": [],
 *                         "hasMore": false,
 *                         "url": "/v1/charges?payment_intent=pi_3Lg5vEIa4mjYLXpf1dpSlpx5"
 *                     },
 *                     "client_secret": "pi_3Lg5vEIa4mjYLXpf1dpSlpx5_secret_DHxKUlFIXlx5cxw1rv1KkPhe1",
 *                     "confirmation_method": "automatic",
 *                     "created": 1662724980,
 *                     "currency": "usd",
 *                     "customer": {
 *                         "id": "cus_MOtfN6d4Ouavi0"
 *                     },
 *                     "description": "Subscription creation",
 *                     "id": "pi_3Lg5vEIa4mjYLXpf1dpSlpx5",
 *                     "invoice": {
 *                         "id": "in_1Lg5vEIa4mjYLXpfEmqZsTRc"
 *                     },
 *                     "livemode": false,
 *                     "metadata": {},
 *                     "object": "payment_intent",
 *                     "payment_method_options": {
 *                         "card": {
 *                             "request_three_d_secure": "automatic"
 *                         },
 *                         "us_bank_account": {
 *                             "verification_method": "automatic"
 *                         }
 *                     },
 *                     "payment_method_types": [
 *                         "card",
 *                         "us_bank_account"
 *                     ],
 *                     "setup_future_usage": "off_session",
 *                     "status": "requires_payment_method"
 *                 }
 *             },
 *             "payment_settings": {},
 *             "period_end": 1662724980,
 *             "period_start": 1662724980,
 *             "post_payment_credit_notes_amount": 0,
 *             "pre_payment_credit_notes_amount": 0,
 *             "starting_balance": 0,
 *             "status": "open",
 *             "status_transitions": {
 *                 "finalized_at": 1662724980
 *             },
 *             "subscription": {
 *                 "id": "sub_1Lg5vEIa4mjYLXpfdRlYQ7Bi"
 *             },
 *             "subtotal": 1500,
 *             "subtotal_excluding_tax": 1500,
 *             "total": 1500,
 *             "total_discount_amounts": [],
 *             "total_excluding_tax": 1500,
 *             "total_tax_amounts": [],
 *             "webhooks_delivered_at": 1662724980
 *         }
 *     }
*/



 /*     "livemode": false,
 *     "metadata": {},
 *     "object": "subscription",
 *     "payment_settings": {
 *         "save_default_payment_method": "on_subscription"
 *     },
 *     "start_date": 1662724980,
 *     "status": "incomplete"
 * }
 */