package ma.project.GedforSaas.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ma.project.GedforSaas.abstractSupperClasses.AbstractSupperClass;

@Entity
@Table(name = "plan")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Plan extends AbstractSupperClass {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "plan_id")
	private Long id;
	private String title;
	private String subTitle;
	private String typeSubscription; //monthly || yearly
	//  currency like usd, eur ...
	private String currency = "USD";
	private String amount;
	private String oldPrice;
	private Long nbDocuments;
	private Long nbUsers;
	private Long nbResources;
	private String userLicence;
	private String productId;// Product ID stripe
	private String priceId;// Price ID stripe
	private String description;

	public Plan(Long nbDocuments, Long nbUsers, String userLicence) {
		super();
		this.nbDocuments = nbDocuments;
		this.nbUsers = nbUsers;
		this.userLicence = userLicence;
	}

}
