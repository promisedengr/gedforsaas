package ma.project.GedforSaas.model;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.Email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.project.GedforSaas.abstractSupperClasses.AbstractSupperClass;

@Audited
@Entity
@Table(name = "resource")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resource extends AbstractSupperClass {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ressource_id")
	private Long id;

	private String firstName;

	private String lastName;
	
	private boolean trash =false; 

	@Email
	@Column(name = "personalEmail", nullable = false, unique = true)
	private String personalEmail;

	@Email
	private String professionalEmail;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date birthDate;

	private String adress;

	private String city;

	private String country;

	private String campagnyName;

	private Long phoneNumber;

	private boolean active;
	
	

	@NotAudited
	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;

	@NotAudited
	@OneToMany(mappedBy = "resource", cascade = CascadeType.REMOVE)
	@JsonIgnore
	private List<Meeting> meetings;

	@NotAudited
	@ManyToMany
	@JoinTable(name = "Resources_Category", joinColumns = @JoinColumn(name = "resource_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
	private List<Category> categories;

	public Resource(String firstName, String lastName, String personalEmail, String professionalEmail, Date birthDate,
			String adress, String city, String country, String campagnyName, Long phoneNumber) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.personalEmail = personalEmail;
		this.professionalEmail = professionalEmail;
		this.birthDate = birthDate;
		this.adress = adress;
		this.city = city;
		this.country = country;
		this.campagnyName = campagnyName;
		this.phoneNumber = phoneNumber;

	}

}
