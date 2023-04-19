package ma.project.GedforSaas.model;

import java.util.Date;
import java.util.List;

import javax.persistence.*;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.project.GedforSaas.abstractSupperClasses.AbstractSupperClass;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription extends AbstractSupperClass {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String reference;// Stripe Subscription ID
    private String customerId;
    private String currentPeriod;
    private String paymentMethod;
    private String nextInvoices;
    private String status;
    private String description;
    private String clientSecret;
    @JsonFormat(pattern = "MM dd yyyy hh:mm")
    private Date expiresDate;
    @JsonFormat(pattern = "MM dd yyyy hh:mm")
    private String periodStart;
    @JsonFormat(pattern = "MM dd yyyy hh:mm")
    private String periodEnd;

    @ManyToOne
    private User user;


    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Invoice> invoices;

}
